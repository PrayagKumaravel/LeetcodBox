package com.example.LeetcodeBox.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.UserRequestDto;
import com.example.LeetcodeBox.Dto.UserResponseDto;
import com.example.LeetcodeBox.Entity.UserEntity;
import com.example.LeetcodeBox.Exception.EntryExistsAlreadyException;
import com.example.LeetcodeBox.Repository.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseWrapperDto CreateUser(UserRequestDto userRequestDto){
        Optional<UserEntity> checker = userRepository.findByName(userRequestDto.getName());
        if(checker.isPresent()){
            throw new EntryExistsAlreadyException("User already exist with name "+userRequestDto.getName());
        }
        String hash=BCrypt.withDefaults().hashToString(12, userRequestDto.getPassword().toCharArray());
        UserEntity userEntity=UserEntity.builder()
        .name(userRequestDto.getName())
        .mailId(userRequestDto.getMailId())
        .password(hash)
        .build();
        UserEntity record=userRepository.save(userEntity);
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
