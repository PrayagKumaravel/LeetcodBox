package com.example.LeetcodeBox.Controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tag")
public class TagController {
    private final TagService tagService;

    @PostMapping("/create/{name}")
    public ResponseWrapperDto CreateTag(@PathVariable String name){
        return tagService.CreateTag(name);
    }
    
    @GetMapping("/get-all")
    public ResponseWrapperDto GetAllTags(){
        return tagService.GetAllTags();
    }

    @DeleteMapping("/delete/{name}")
    public ResponseWrapperDto DeleteTag(@PathVariable String name){
        return tagService.DeleteTag(name);
    }
}
