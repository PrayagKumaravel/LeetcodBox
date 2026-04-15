package com.example.LeetcodeBox.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeetcodeBox.Service.PredictionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/groq/test")
public class dummy {
    private final PredictionService predictionService;

    @PostMapping("/process")
    public String dummyprocess(@RequestParam String content, @RequestParam String title,@RequestParam String lang){
        return predictionService.ProcessWithGroq(content,title,lang);
    }
}
