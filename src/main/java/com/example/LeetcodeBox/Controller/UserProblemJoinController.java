package com.example.LeetcodeBox.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.UserProblemJoinRequestDto;
import com.example.LeetcodeBox.Dto.UserRequestDto;
import com.example.LeetcodeBox.Service.UserProblemJoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserProblemJoinController {
    private final UserProblemJoinService userProblemJoinService;

    @PostMapping("/assign-user-problem")
    public ResponseWrapperDto AssignUserProblem(@RequestBody UserProblemJoinRequestDto userProblemJoinRequestDto){
        return userProblemJoinService.AssignUserProblem(userProblemJoinRequestDto);
    }

    @GetMapping("/details")
    public ResponseWrapperDto GetUserDetails(@RequestBody UserRequestDto userRequestDto){
        return userProblemJoinService.GetUserDetails(userRequestDto);
    }
}
