package com.example.LeetcodeBox.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ProblemRequestDto;
import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.TagRequestDto;
import com.example.LeetcodeBox.Entity.ProblemEntity;
import com.example.LeetcodeBox.Entity.TagEntity;
import com.example.LeetcodeBox.Exception.EntryDoesntExistsException;
import com.example.LeetcodeBox.Exception.EntryExistsAlreadyException;
import com.example.LeetcodeBox.Exception.InvalidInputException;
import com.example.LeetcodeBox.Exception.JoinTableException;
import com.example.LeetcodeBox.Repository.ProblemRepository;
import com.example.LeetcodeBox.Repository.TagRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {
    private final SideService sideService;
    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;


    public ResponseWrapperDto CreateProblem(ProblemRequestDto problemRequestDto){
        //if already problem exist
        if(problemRepository.findByUrl(problemRequestDto.getUrl()).isPresent() ||
            problemRepository.findByTitle(problemRequestDto.getTitle()).isPresent()){
            ///try inserting new tags
            ///problem Exist already
            throw new EntryExistsAlreadyException("This Problem Exists already");
        }
        //check whether given tags are in Tag db already
        if(!sideService.tagsExist(problemRequestDto.getTags())){
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

    //update problem tag, add remove
    //fix using url (later)
    public ResponseWrapperDto GetProblemDetails(String title){
        Optional<ProblemEntity> record=problemRepository.findByTitle(title);
        if(record.isEmpty()){
            throw new EntryDoesntExistsException("Problem with title "+title+" doesn't exist");
        }

        Set<TagEntity> tagEntities=record.get().getTags();
        Set<TagRequestDto> tagRequestDtos=new HashSet<>();
        for(TagEntity tagEntity:tagEntities){
            tagRequestDtos.add(
                TagRequestDto.builder()
                .name(tagEntity.getName())
                .build()
            );
        }

        return ResponseWrapperDto.builder()
        .status(200)
        .message("Sucessfully fetched")
        .problem(
            ProblemRequestDto.builder()
            .title(record.get().getTitle())
            .url(record.get().getUrl())
            .tags(tagRequestDtos)
            .build()
        )
        .build();
    }
    //get problems with tag x (of user y)

    //mukltiple tags (upgrade done)
    public ResponseWrapperDto GetProblemsOfTag(ProblemRequestDto problemRequestDto){

        Set<TagRequestDto> tag=problemRequestDto.getTags();
        Set<String> names=new HashSet<>();
        for(TagRequestDto tagRequestDto:tag){
            if(tagRequestDto.getName()==null || tagRequestDto.getName().trim().length()==0){
                throw new InvalidInputException("Tag Name is Required");
            }
            names.add(tagRequestDto.getName().toUpperCase());
        }
        Set<TagEntity> tagEntities=tagRepository.findByNameIn(names);
        if(tagEntities.isEmpty()){
            throw new EntryDoesntExistsException("Sorry No tags named this exists");
        }

        Set<ProblemEntity> problemEntities=problemRepository.findByTagsIn(tagEntities);
        List<ProblemRequestDto> problemRequestDtos=new ArrayList<>();
        for(ProblemEntity problemEntity:problemEntities){
            ProblemRequestDto problemDto=sideService.ProblemEntityToDto(problemEntity);
            if(!problemRequestDtos.contains(problemDto)){
                problemRequestDtos.add(problemDto);
            }
        }
        
        return ResponseWrapperDto.builder()
        .status(200)
        .message("Sucessfully fetched")
        .problems(problemRequestDtos)
        .build();
    }
}
