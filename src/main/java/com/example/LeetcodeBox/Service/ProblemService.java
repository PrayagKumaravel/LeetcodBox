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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProblemService {
    private final SideService sideService;
    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;


    public ResponseWrapperDto CreateProblem(ProblemRequestDto problemRequestDto){
        //if already problem exist
        if(problemRepository.findByUrl(problemRequestDto.getUrl()).isPresent() ||
            problemRepository.findByTitle(problemRequestDto.getTitle()).isPresent()){
            
            ///problem Exist already
            log.error("This problem Exists already");
            throw new EntryExistsAlreadyException("This Problem Exists already");
        }
        //check whether given tags are in Tag db already
        if(!sideService.tagsExist(problemRequestDto.getTags())){
            //try inserting new tags
            log.error("Some tags specified are not in TagEntity");
            throw new JoinTableException("Some tags specified are not in Tag Entity");
        }
        Set<String> names=new HashSet<>();
        for(TagRequestDto tagRequestDto:problemRequestDto.getTags()){
            names.add(
                tagRequestDto.getName().toUpperCase()
            );
        }
        Set<TagEntity> tagEntities=tagRepository.findByNameIn(names);
        log.info("Records found for all tags");
        ProblemEntity problemEntity=ProblemEntity.builder()
        .title(problemRequestDto.getTitle().toUpperCase())
        .url(problemRequestDto.getUrl())
        .tags(tagEntities)
        .build();
        ProblemEntity record=problemRepository.save(problemEntity);
        log.info("Record saved sucessfully");
        Set<TagRequestDto> tagRequestDtos=new HashSet<>();
        for(TagEntity tagEntity:record.getTags()){
            tagRequestDtos.add(TagRequestDto.builder()
            .name(tagEntity.getName())
            .build()
            );
        }
        
        log.info("Problem Created sucessfully");
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
            log.error("Problem with title "+title+" doesn't exist");
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

        log.info("Problem with title "+title+" was sucessfully fetched");
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
    public ResponseWrapperDto GetProblemsOfTags(ProblemRequestDto problemRequestDto){

        Set<TagRequestDto> tag=problemRequestDto.getTags();
        Set<String> names=new HashSet<>();
        for(TagRequestDto tagRequestDto:tag){
            if(tagRequestDto.getName()==null || tagRequestDto.getName().trim().length()==0){
                log.error("Tag name is required");
                throw new InvalidInputException("Tag Name is Required");
            }
            names.add(tagRequestDto.getName().toUpperCase());
        }
        Set<TagEntity> tagEntities=tagRepository.findByNameIn(names);
        if(tagEntities.isEmpty()){
            log.error("No single tag exists");
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
        
        log.info("Sucessfully fetched problems of given tags");
        return ResponseWrapperDto.builder()
        .status(200)
        .message("Sucessfully fetched")
        .problems(problemRequestDtos)
        .build();
    }
//update url of problem based on user choice, when user input title is already in db
}
