package com.example.LeetcodeBox.Dto;

import java.util.List;

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
public class GroqResponseDto {
    private String id;

    private String object;

    private Long created;

    private String model;

    private List<ChoiceResponseDto> choices;

    private UsageResponseDto usage;

    private String usage_breakdown;

    private String system_fingerprint;

    private XGroqResponseDto x_groq;

    private String service_tier;
}
