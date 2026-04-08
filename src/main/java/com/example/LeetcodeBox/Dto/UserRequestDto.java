package com.example.LeetcodeBox.Dto;

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
public class UserRequestDto {
    @NotBlank(message ="Name is required")
    private String name;

    @NotBlank(message="MailId is required")
    private String mailId;

    @NotBlank(message ="Password is required")
    private String password;

    private String  username;
}
