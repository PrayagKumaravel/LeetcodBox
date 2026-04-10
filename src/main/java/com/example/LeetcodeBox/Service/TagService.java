package com.example.LeetcodeBox.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.TagRequestDto;
import com.example.LeetcodeBox.Entity.TagEntity;
import com.example.LeetcodeBox.Exception.EmptyEntityException;
import com.example.LeetcodeBox.Exception.EntryExistsAlreadyException;
import com.example.LeetcodeBox.Exception.InvalidInputException;
import com.example.LeetcodeBox.Repository.TagRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository tagRepository;

    public ResponseWrapperDto CreateTag(String name){
        if(name==null){
            throw new InvalidInputException("Invalid Input");
        }
        name=name.trim();
        if(name.length()==0){
            throw new InvalidInputException("Invalid Input");
        }
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
        if(name==null){
            throw new InvalidInputException("Invalid Input");
        }
        name=name.trim();
        if(name.length()==0){
            throw new InvalidInputException("Invalid Input");
        }
        tagRepository.deleteByName(name.toUpperCase());
        return ResponseWrapperDto.builder().status(200).message("Deletion done sucessfully").build();
    }
    
    //called from problemservice 
    public boolean tagsExist(Set<TagRequestDto> tagRequestDtos){
        for(TagRequestDto tagRequestDto:tagRequestDtos){
            Optional<TagEntity> record=tagRepository.findByName(tagRequestDto.getName().toUpperCase());
            if(record.isEmpty()){
                return false;
            }
        }
        return true;
    }

}
