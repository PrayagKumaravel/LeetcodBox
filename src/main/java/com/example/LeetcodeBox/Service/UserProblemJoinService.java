package com.example.LeetcodeBox.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ProblemRequestDto;
import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.UserProblemJoinRequestDto;
import com.example.LeetcodeBox.Dto.UserRequestDto;
import com.example.LeetcodeBox.Entity.ProblemEntity;
import com.example.LeetcodeBox.Entity.UserEntity;
import com.example.LeetcodeBox.Entity.UserProblemJoinEntity;
import com.example.LeetcodeBox.Enum.SolvingStatusEnum;
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
    private final PredictionService predictionService;

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

        String lang=userProblemJoinRequestDto.getLang()!=null?userProblemJoinRequestDto.getLang().toString():null;

        UserProblemJoinEntity userProblemJoinEntity=UserProblemJoinEntity.builder()
        .user(userRecord.get())
        .problem(problemRecord.get())
        //check this working once 
        .notes(userProblemJoinRequestDto.getMode().toString()=="MANUAL"?userProblemJoinRequestDto.getNotes():predictionService.ProcessWithGroq(userProblemJoinRequestDto.getContent(),problemRecord.get().getTitle(),lang))
        .status(userProblemJoinRequestDto.getStatus())
        .frequency(1l)
        .solved_frequency(userProblemJoinRequestDto.getStatus().toString()=="SOLVED"?1l:0l)
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
   
    public ResponseWrapperDto UpdateUserProblemDetail(UserProblemJoinRequestDto userProblemJoinRequestDto){
        
        if(userProblemJoinRequestDto.getUser().getMailId()==null || 
           userProblemJoinRequestDto.getUser().getMailId().length()==0 ||
            userProblemJoinRequestDto.getProblem().getUrl()==null ||
            userProblemJoinRequestDto.getProblem().getUrl().trim().length()==0){
                throw new InvalidInputException("Invalid Entry");
           }

        Optional<ProblemEntity> problemEntity=problemRepository.findByUrl(userProblemJoinRequestDto.getProblem().getUrl());
        Optional<UserEntity> userEntity=userRepository.findByMailId(userProblemJoinRequestDto.getUser().getMailId());

        if(problemEntity.isEmpty() || userEntity.isEmpty()){
            throw new EntryDoesntExistsException("No entry exist with this user problem detail");
        }
        List<UserProblemJoinEntity> userProblemJoinEntities=userProblemJoinRepository.findByProblemAndUser(problemEntity.get(),userEntity.get());

        if(userProblemJoinEntities.isEmpty()){
            throw new EntryDoesntExistsException("Sorry, No entry with these details");
        }
        
        UserProblemJoinEntity userProblemJoinEntity=userProblemJoinEntities.get(0);
        SolvingStatusEnum statusEnum=!userProblemJoinEntity.getStatus().toString().equals("SOLVED")?
            userProblemJoinRequestDto.getStatus():userProblemJoinEntity.getStatus();
        
         String lang=userProblemJoinRequestDto.getLang()!=null?userProblemJoinRequestDto.getLang().toString():null;

        Long prev_frequency=userProblemJoinEntity.getFrequency();

        userProblemJoinEntity.setNotes(userProblemJoinRequestDto.getMode().toString()=="MANUAL"?userProblemJoinRequestDto.getNotes():predictionService.ProcessWithGroq(userProblemJoinRequestDto.getContent(),userProblemJoinRequestDto.getProblem().getTitle(),lang));
        userProblemJoinEntity.setStatus(statusEnum);
        userProblemJoinEntity.setFrequency(userProblemJoinEntity.getFrequency()+1);
        if(userProblemJoinRequestDto.getStatus().toString().equals("SOLVED")){
            userProblemJoinEntity.setSolved_frequency(userProblemJoinEntity.getSolved_frequency()+1);
        }
        //fix
        if(userProblemJoinRequestDto.getTime()>60){
            userProblemJoinRequestDto.setTime(60l);
        }
        userProblemJoinEntity.setAverage_time(((prev_frequency*userProblemJoinEntity.getAverage_time())+userProblemJoinRequestDto.getTime())/(prev_frequency+1));        
        
        userProblemJoinRepository.save(userProblemJoinEntity);

        return ResponseWrapperDto.builder()
        .status(200)
        .message("Updation Sucessful")
        .build();
    }
//SAME AS GET USER DETAILS
    public ResponseWrapperDto FetchAllProblemOfUser(UserRequestDto userRequestDto){
        List<UserProblemJoinRequestDto> userproblemDetails=GetUserDetails(userRequestDto).getUser_problem_details();
        List<ProblemRequestDto> problemRequestDtos=new ArrayList<>();

        for(UserProblemJoinRequestDto userProblemJoinRequestDto:userproblemDetails){
            problemRequestDtos.add(userProblemJoinRequestDto.getProblem());
        }

        return ResponseWrapperDto.builder()
        .status(200)
        .message("Fetched sucessful")
        .problems(problemRequestDtos)
        .build();
    }
}
