package com.example.LeetcodeBox.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsageResponseDto {
    private Double queue_time;

    private Long prompt_tokens;

    private Double prompt_time;

    private Long completion_tokens;

    private Long completion_time;

    private Long total_tokens;

    private Long total_time;
}
