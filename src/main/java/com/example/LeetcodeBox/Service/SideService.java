package com.example.LeetcodeBox.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.LeetcodeBox.Dto.ProblemRequestDto;
import com.example.LeetcodeBox.Dto.ResponseWrapperDto;
import com.example.LeetcodeBox.Dto.TagRequestDto;
import com.example.LeetcodeBox.Dto.UserProblemJoinRequestDto;
import com.example.LeetcodeBox.Dto.UserRequestDto;
import com.example.LeetcodeBox.Entity.ProblemEntity;
import com.example.LeetcodeBox.Entity.TagEntity;
import com.example.LeetcodeBox.Entity.UserEntity;
import com.example.LeetcodeBox.Entity.UserProblemJoinEntity;
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

    public UserProblemJoinRequestDto UserProblemJoinEntityToDto(UserProblemJoinEntity userProblemJoinEntity){
        return  UserProblemJoinRequestDto.builder()
                .user(UserEntityToDto(userProblemJoinEntity.getUser()))
                .problem(ProblemEntityToDto(userProblemJoinEntity.getProblem()))
                .notes(userProblemJoinEntity.getNotes())
                .status(userProblemJoinEntity.getStatus())
                .rate(((double)userProblemJoinEntity.getSolved_frequency()/(double)userProblemJoinEntity.getFrequency())*100)
                .time(userProblemJoinEntity.getAverage_time())
                .build();
    }

    public UserRequestDto UserEntityToDto(UserEntity userEntity){

        return UserRequestDto.builder()
        .name(userEntity.getName())
        .mailId(userEntity.getMailId())
        .build();
    }

    public ProblemRequestDto ProblemEntityToDto(ProblemEntity problemEntity){
        Set<TagRequestDto> tagRequestDtos=new HashSet<>();
        for(TagEntity tagEntity:problemEntity.getTags()){
            tagRequestDtos.add(TagEntityToDto(tagEntity));
        }
        return ProblemRequestDto.builder()
        .title(problemEntity.getTitle())
        .url(problemEntity.getUrl())
        .tags(tagRequestDtos)
        .build();
    }
    
    public TagRequestDto TagEntityToDto(TagEntity tagEntity){
        return TagRequestDto.builder()
        .name(tagEntity.getName())
        .build();
    }

    public ProblemEntity ProblemRequestDtoToEntity(ProblemRequestDto problemRequestDto){

        Set<TagEntity> tagEntities=new HashSet<>();
        for(TagRequestDto tagRequestDto:problemRequestDto.getTags()){
            tagEntities.add(TagRequestDtoToEntity(tagRequestDto));
        }

        return ProblemEntity.builder()
        .title(problemRequestDto.getTitle())
        .url(problemRequestDto.getUrl())
        .tags(tagEntities)
        .build();
    }

    public UserEntity UserRequestDtoToEntity(UserRequestDto userRequestDto){
        return UserEntity.builder()
        .name(userRequestDto.getName())
        .mailId(userRequestDto.getMailId())
        .build();
    }

    public TagEntity TagRequestDtoToEntity(TagRequestDto tagRequestDto){
        return TagEntity.builder()
        .name(tagRequestDto.getName())
        .build();
    }
}
