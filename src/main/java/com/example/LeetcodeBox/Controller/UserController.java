package com.example.LeetcodeBox.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.UserRequestDto;
import com.example.LeetcodeBox.Dto.UserUpdationDto;
import com.example.LeetcodeBox.Service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("get-all")
    public ResponseWrapperDto GetAllUser(){
        return userService.GetAll();
    }

    @PatchMapping("/update")
    public ResponseWrapperDto UpdateMailId(@RequestBody UserUpdationDto userUpdationDto){
        return userService.UpdateMailId(userUpdationDto);
    }
}
