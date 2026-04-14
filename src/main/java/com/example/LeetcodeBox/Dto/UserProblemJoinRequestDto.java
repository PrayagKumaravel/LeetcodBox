package com.example.LeetcodeBox.Dto;

import com.example.LeetcodeBox.Enum.ModeEnum;
import com.example.LeetcodeBox.Enum.SolvingStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserProblemJoinRequestDto {
    @NotBlank(message="UserName is required")
    private UserRequestDto user;

    @NotBlank(message = "Problem Name is required")
    private ProblemRequestDto problem;

    private ModeEnum mode;

    private String content;

    private String notes;

    private SolvingStatusEnum status;

    private Double rate;

    private Long time;
}
