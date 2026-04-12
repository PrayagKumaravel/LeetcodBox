package com.example.LeetcodeBox.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.catalina.User;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.UserProblemJoinRequestDto;
import com.example.LeetcodeBox.Dto.UserRequestDto;
import com.example.LeetcodeBox.Entity.ProblemEntity;
import com.example.LeetcodeBox.Entity.UserEntity;
import com.example.LeetcodeBox.Entity.UserProblemJoinEntity;
import com.example.LeetcodeBox.Exception.EntryDoesntExistsException;
import com.example.LeetcodeBox.Exception.EntryExistsAlreadyException;
import com.example.LeetcodeBox.Exception.InvalidInputException;
import com.example.LeetcodeBox.Repository.ProblemRepository;
import com.example.LeetcodeBox.Repository.UserProblemJoinRepository;
import com.example.LeetcodeBox.Repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProblemJoinService {
    private final UserProblemJoinRepository userProblemJoinRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;


    private final ProblemService problemService;
    private final SideService sideService;

    public ResponseWrapperDto AssignUserProblem(UserProblemJoinRequestDto userProblemJoinRequestDto){
        
        Optional<UserEntity> userRecord=userRepository.findByMailId(userProblemJoinRequestDto.getUser().getMailId());
        if(userRecord.isEmpty()){
            throw new EntryDoesntExistsException("User doesn't exist with mailId "+userProblemJoinRequestDto.getUser().getMailId());
        }

        Optional<ProblemEntity> problemRecord=problemRepository.findByUrl(userProblemJoinRequestDto.getProblem().getUrl());
        if(problemRecord.isEmpty()){
            problemService.CreateProblem(userProblemJoinRequestDto.getProblem());
            problemRecord=problemRepository.findByUrl(userProblemJoinRequestDto.getProblem().getUrl());
        }
        UserProblemJoinEntity userProblemJoinEntity=UserProblemJoinEntity.builder()
        .user(userRecord.get())
        .problem(problemRecord.get())
        .notes(userProblemJoinRequestDto.getNotes())
        .status(userProblemJoinRequestDto.getStatus())
        .average_time(userProblemJoinRequestDto.getTime())
        .build();

        List<UserProblemJoinEntity> exist=userProblemJoinRepository.findByProblemAndUser(problemRecord.get(),userRecord.get());
        if(!exist.isEmpty()){
            throw new EntryExistsAlreadyException("Already user with this particular problem details exist");
        }
        UserProblemJoinEntity record=userProblemJoinRepository.save(userProblemJoinEntity);

        return ResponseWrapperDto.builder()
        .status(200)
        .message("Problem info has been stored to user "+record.getUser().getMailId())
        .build();
    }

    public ResponseWrapperDto GetUserDetails(UserRequestDto userRequestDto){

        if(userRequestDto.getMailId()==null || userRequestDto.getMailId().trim().length()==0){
            throw new InvalidInputException("Mail Id Required");
        }

        Optional<UserEntity> userEntity=userRepository.findByMailId(userRequestDto.getMailId());
        if(userEntity.isEmpty()){
            throw new EntryDoesntExistsException("User doesn't exist");
        }
        List<UserProblemJoinEntity> userProblemJoinEntities=userProblemJoinRepository.findByUser(userEntity.get());
        List<UserProblemJoinRequestDto> userProblemJoinRequestDtos=new ArrayList<>();
        for(UserProblemJoinEntity userProblemJoinEntity:userProblemJoinEntities){
            userProblemJoinRequestDtos.add(sideService.UserProblemJoinEntityToDto(userProblemJoinEntity));
        }

        return ResponseWrapperDto.builder()
        .status(200)
        .message("Sucessfully fetched")
        .user_problem_details(userProblemJoinRequestDtos)
        .build();
    }

}
