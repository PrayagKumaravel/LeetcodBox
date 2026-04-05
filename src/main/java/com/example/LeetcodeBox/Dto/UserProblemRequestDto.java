package com.example.LeetcodeBox.Dto;

import com.example.LeetcodeBox.Enum.SolvingStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
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
public class UserProblemRequestDto {
    @NotBlank(message="UserName is required")
    private String user_name;

    @NotBlank(message = "Problem Name is required")
    private String problem_name;

    private String notes;

    private SolvingStatusEnum status;

    private Long time;
}
