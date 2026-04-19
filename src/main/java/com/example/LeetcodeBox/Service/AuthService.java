package com.example.LeetcodeBox.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.UserRequestDto;
import com.example.LeetcodeBox.Dto.UserResponseDto;
import com.example.LeetcodeBox.Entity.UserEntity;
import com.example.LeetcodeBox.Exception.EntryExistsAlreadyException;
import com.example.LeetcodeBox.Exception.InvalidInputException;
import com.example.LeetcodeBox.Repository.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;

    public ResponseWrapperDto SignUp(UserRequestDto userRequestDto){
        if(userRequestDto.getMailId()==null || 
            userRequestDto.getMailId().trim().length()==0 ||
            userRequestDto.getPassword()==null ||
            userRequestDto.getPassword().trim().length()==0){
                log.error("Invalid Parameter - Some parameters are missing");
            throw new InvalidInputException("Invalid Parameter");
        }
        Optional<UserEntity> checker = userRepository.findByMailId(userRequestDto.getMailId().trim());
        if(checker.isPresent()){
            log.error("User already exist with Mail "+userRequestDto.getMailId());
            throw new EntryExistsAlreadyException("User already exist with Mail "+userRequestDto.getMailId());
        }
        //password to be hashed -> password + salt
        //hashed password structure-> version+cost+salt+password
        //creaters hasher object -> add salt and use algo to hash to string(both  by hashtoString(cost,password))
        String hash=BCrypt.withDefaults().hashToString(12, userRequestDto.getPassword().toCharArray());
        UserEntity userEntity=UserEntity.builder()
        .name(userRequestDto.getName())
        .mailId(userRequestDto.getMailId().trim())
        .password(hash)
        .build();
        UserEntity record=userRepository.save(userEntity);
        log.info("User SignedUp Sucessfully");
        return ResponseWrapperDto.builder().user(
            UserResponseDto.builder()
            .name(record.getName())
            .build()
        )
        .status(200)
        .message("User created Sucessfully")
        .build();
    }
}
