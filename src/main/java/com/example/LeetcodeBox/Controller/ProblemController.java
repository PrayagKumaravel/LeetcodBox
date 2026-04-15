package com.example.LeetcodeBox.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeetcodeBox.Dto.ProblemRequestDto;
import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.TagRequestDto;
import com.example.LeetcodeBox.Service.ProblemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/problem")
public class ProblemController {
    private final ProblemService problemService;

    @PostMapping("create")
    public ResponseWrapperDto CreateProblem(@RequestBody ProblemRequestDto problemRequestDto){
        return problemService.CreateProblem(problemRequestDto);
    }

    @GetMapping("/details")
    public ResponseWrapperDto GetProblemDetails(@RequestParam String title){
        return problemService.GetProblemDetails(title);
    }

    @GetMapping("/tag")
    public ResponseWrapperDto GetProblemsOfTag(@RequestBody TagRequestDto tagRequestDto){
        return problemService.GetProblemsOfTag(tagRequestDto);
    }


}
