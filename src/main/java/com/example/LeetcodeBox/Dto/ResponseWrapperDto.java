package com.example.LeetcodeBox.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapperDto {

    //error code
    private Long status;
    private String message;

    //pagination
    private Long total_elements;
    private Long total_pages;
    private Long page_number;
    private Long size;

    //optional
    private UserResponseDto user;
    private List<UserResponseDto> users;

    private TagRequestDto tag;
    private List<TagRequestDto> tags;

    private ProblemRequestDto problem;
    private List<ProblemRequestDto> problems;

    private UserProblemRequestDto user_problem_detail;
    private List<UserProblemRequestDto> user_problem_details;
}
