package com.example.LeetcodeBox.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.UserResponseDto;
import com.example.LeetcodeBox.Dto.UserUpdationDto;
import com.example.LeetcodeBox.Entity.UserEntity;
import com.example.LeetcodeBox.Exception.EmptyEntityException;
import com.example.LeetcodeBox.Exception.EntryDoesntExistsException;
import com.example.LeetcodeBox.Exception.InvalidInputException;
import com.example.LeetcodeBox.Exception.WrongPasswordException;
import com.example.LeetcodeBox.Repository.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public ResponseWrapperDto GetAll(){
        List<UserEntity> userEntities=userRepository.findAll();
        if(userEntities.isEmpty()){
            throw new EmptyEntityException("User Entity is Empty");
        }
        List<UserResponseDto> userResponseDtos=new ArrayList<>();

        for(UserEntity userEntity:userEntities){
            userResponseDtos.add(
                UserResponseDto.builder()
                .name(userEntity.getName())
                .mailId(userEntity.getMailId().trim())
                .username(userEntity.getUsername())
                .build()
            );
        }

        return ResponseWrapperDto.builder().status(200).users(userResponseDtos).build();
    }

    public ResponseWrapperDto UpdateMailId(UserUpdationDto userRequestDto){
        if(userRequestDto.getMailId()==null || 
            userRequestDto.getMailId().trim().length()==0|| 
            userRequestDto.getNewMailId()==null || 
            userRequestDto.getMailId().trim().length()==0 ||
            userRequestDto.getPassword()==null ||
            userRequestDto.getPassword().trim().length()==0){
            throw new InvalidInputException("Some parameters are missing");
        }
        Optional<UserEntity> userEntity=userRepository.findByMailId(userRequestDto.getMailId().trim());
        if(userEntity.isEmpty()){
            throw new EntryDoesntExistsException("No user with this MailId");
        }
        UserEntity record=userEntity.get();
        if(!BCrypt.verifyer().verify(userRequestDto.getPassword().toCharArray(),record.getPassword()).verified){
            throw new WrongPasswordException("Wrong password");
        }
        record.setMailId(userRequestDto.getNewMailId().trim());
        UserEntity updated_record=userRepository.save(record);
        return ResponseWrapperDto.builder().status(200)
        .message("MailId updated sucessfully")
        .user(
            UserResponseDto.builder()
            .name(updated_record.getName())
            .mailId(updated_record.getMailId().trim())
            .username(updated_record.getUsername())
            .build()
        )
        .build();
    }

    public ResponseWrapperDto UpdatePassword(UserUpdationDto userUpdationDto){
        if(userUpdationDto.getPassword()==null ||
            userUpdationDto.getPassword().trim().length()==0 ||
            userUpdationDto.getNewPassword()==null || 
            userUpdationDto.getNewPassword().trim().length()==0 || 
            userUpdationDto.getMailId()==null || 
            userUpdationDto.getMailId().trim().length()==0){
            throw new InvalidInputException("Input parameters are missing");
        }
        Optional<UserEntity> userEntity=userRepository.findByMailId(userUpdationDto.getMailId().trim());
        if(userEntity.isEmpty()){
            throw new EntryDoesntExistsException("User doesnt exist with this mail id");
        }
        UserEntity record=userEntity.get();
        if(!BCrypt.verifyer().verify(userUpdationDto.getPassword().trim().toCharArray(),record.getPassword().toCharArray()).verified){
            throw new WrongPasswordException("Password access denied");
        }
        String hash=BCrypt.withDefaults().hashToString(12,userUpdationDto.getNewPassword().trim().toCharArray());
        record.setPassword(hash);
        UserEntity updated_record=userRepository.save(record);
        return ResponseWrapperDto.builder().status(200).message("Password updated sucessfully")
        .user(
            UserResponseDto.builder()
            .name(updated_record.getName())
            .mailId(updated_record.getMailId())
            .build()
        )
        .build();
    }
}
