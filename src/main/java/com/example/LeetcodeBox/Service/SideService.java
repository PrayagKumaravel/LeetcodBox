package com.example.LeetcodeBox.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Entity.ProblemEntity;
import com.example.LeetcodeBox.Entity.TagEntity;
import com.example.LeetcodeBox.Repository.ProblemRepository;
import com.example.LeetcodeBox.Repository.TagRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SideService {
    private final TagRepository tagRepository;
    private final ProblemRepository problemRepository;

    public ResponseWrapperDto UnlinkTag(TagEntity tagEntity){
        tagRepository.findByName(tagEntity.getName().toUpperCase());
        //all problem -> iterate-> remove tag entity
        List<ProblemEntity> problemEntities=problemRepository.findAll();
        for(ProblemEntity problemEntity:problemEntities){
            problemEntity.getTags().remove(tagEntity);
            problemRepository.save(problemEntity);
        }
        return ResponseWrapperDto.builder()
        .status(200)
        .message("Unlinked Tag with Problems")
        .build();
    }
}
