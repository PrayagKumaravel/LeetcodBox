package com.example.LeetcodeBox.Service;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.TagRequestDto;
import com.example.LeetcodeBox.Entity.TagEntity;
import com.example.LeetcodeBox.Repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public ResponseWrapperDto CreateTag(String name){
        TagEntity record=tagRepository.save(TagEntity.builder().name(name).build());
        TagRequestDto tagRequestDto=TagRequestDto.builder().name(record.getName()).build();
        return ResponseWrapperDto.builder().tag(tagRequestDto).build();
    }
}
