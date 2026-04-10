package com.example.LeetcodeBox.Service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ProblemRequestDto;
import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.TagRequestDto;
import com.example.LeetcodeBox.Entity.ProblemEntity;
import com.example.LeetcodeBox.Entity.TagEntity;
import com.example.LeetcodeBox.Exception.EntryExistsAlreadyException;
import com.example.LeetcodeBox.Exception.JoinTableException;
import com.example.LeetcodeBox.Repository.ProblemRepository;
import com.example.LeetcodeBox.Repository.TagRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final TagService tagService;
    private final TagRepository tagRepository;

    public ResponseWrapperDto CreateProblem(ProblemRequestDto problemRequestDto){
        //if already problem exist
        if(problemRepository.findByTitle(problemRequestDto.getTitle().toUpperCase()).isPresent()){
            ///try inserting new tags
            throw new EntryExistsAlreadyException("This Problem Exists already");
        }
        //check whether given tags are in Tag db already
        if(!tagService.tagsExist(problemRequestDto.getTags())){
            throw new JoinTableException("Some tags specified are not in Tag Entity");
        }
        Set<String> names=new HashSet<>();
        for(TagRequestDto tagRequestDto:problemRequestDto.getTags()){
            names.add(
                tagRequestDto.getName().toUpperCase()
            );
        }
        Set<TagEntity> tagEntities=tagRepository.findByNameIn(names);
        ProblemEntity problemEntity=ProblemEntity.builder()
        .title(problemRequestDto.getTitle().toUpperCase())
        .url(problemRequestDto.getUrl())
        .tags(tagEntities)
        .build();
        ProblemEntity record=problemRepository.save(problemEntity);
        Set<TagRequestDto> tagRequestDtos=new HashSet<>();
        for(TagEntity tagEntity:record.getTags()){
            tagRequestDtos.add(TagRequestDto.builder()
            .name(tagEntity.getName())
            .build()
            );
        }
        
        return ResponseWrapperDto.builder()
        .problem(
            ProblemRequestDto.builder()
            .title(record.getTitle().toUpperCase())
            .url(record.getUrl())
            .tags(tagRequestDtos)
            .build()
        )
        .build();
    }

    
}
