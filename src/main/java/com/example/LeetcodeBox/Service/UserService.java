package com.example.LeetcodeBox.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.UserResponseDto;
import com.example.LeetcodeBox.Entity.UserEntity;
import com.example.LeetcodeBox.Repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;

    public ResponseWrapperDto GetAll(){
        List<UserEntity> userEntities=userRepository.findAll();
        List<UserResponseDto> userResponseDtos=new ArrayList<>();

        for(UserEntity userEntity:userEntities){
            userResponseDtos.add(
                UserResponseDto.builder()
                .name(userEntity.getName())
                .mailId(userEntity.getMailId())
                .username(userEntity.getUsername())
                .build()
            );
        }

        return ResponseWrapperDto.builder().status(200).users(userResponseDtos).build();
    }
}
