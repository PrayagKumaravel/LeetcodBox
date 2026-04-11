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
@Transactional// it is used fro rollback and commit, jps used transactional by defult to its default methods
//but for user cutom methods users must implemenet transactional annottaion to implemet these feature
public class TagService {
    private final TagRepository tagRepository;
    private final SideService sideService;

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
        name=name.toUpperCase();
        //since its related with join table(problemtag) -> first cut the link, then delete the tag
        Optional<TagEntity> record=tagRepository.findByName(name);
        if(record.isPresent()){
            sideService.UnlinkTag(
                record.get()
            );
        }
        
        //nw delete tag from tagentity
        tagRepository.deleteByName(name);//will save automatcially since i have used transactional
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
