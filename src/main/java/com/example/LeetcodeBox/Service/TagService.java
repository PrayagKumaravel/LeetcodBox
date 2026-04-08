package com.example.LeetcodeBox.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.TagRequestDto;
import com.example.LeetcodeBox.Entity.TagEntity;
import com.example.LeetcodeBox.Exception.EmptyEntityException;
import com.example.LeetcodeBox.Exception.EntryExistsAlreadyException;
import com.example.LeetcodeBox.Repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public ResponseWrapperDto CreateTag(String name){
        Optional<TagEntity> checker=tagRepository.findByName(name.toUpperCase());
        if(checker.isPresent()){
            throw new EntryExistsAlreadyException(name+" tag already exists");
        }
        TagEntity record=tagRepository.save(TagEntity.builder().name(name.toUpperCase()).build());
        TagRequestDto tagRequestDto=TagRequestDto.builder().name(record.getName()).build();
        return ResponseWrapperDto.builder().status(200).tag(tagRequestDto).build();
    }

    public ResponseWrapperDto GetAllTags(){
        List<TagEntity> tagEntities=tagRepository.findAll();
        if(tagEntities.isEmpty()){
            throw new EmptyEntityException("Tag Entity is Empty");
        }
        List<TagRequestDto> tagRequestDtos=new ArrayList<>();
        for(TagEntity tagEntity:tagEntities){
            tagRequestDtos.add(TagRequestDto.builder()
            .name(tagEntity.getName())
            .build()
        );
        }
        return ResponseWrapperDto.builder().status(200).tags(tagRequestDtos).build();
    }

    public ResponseWrapperDto DeleteTag(String name){
        tagRepository.deleteByName(name.toUpperCase());
        return ResponseWrapperDto.builder().status(200).message("Deletion done sucessfully").build();
    }

}
