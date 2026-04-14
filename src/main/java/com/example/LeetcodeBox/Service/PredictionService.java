package com.example.LeetcodeBox.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.LeetcodeBox.Dto.GroqRequestDto;
import com.example.LeetcodeBox.Dto.GroqResponseDto;
import com.example.LeetcodeBox.Dto.MessageRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PredictionService {


    private final WebClient webClient;

    @Value("${Groq.api.url}")
    private String Groq_api_url;

    @Value("${Groq.api.key}")
    private String Groq_api_key;

    public String ProcessWithGroq(String content,String problem_title){

        //prefered  programming language feature

        List<MessageRequestDto> messageRequestDtos=new ArrayList<>();
        messageRequestDtos.add(MessageRequestDto.builder()
            .role("user")
            .content(BuildContent(content, problem_title))
            .build());
        //buyildinfg request
        GroqRequestDto groqRequestDto=GroqRequestDto.builder()
        .model("llama-3.3-70b-versatile")
        .messages(messageRequestDtos)
        .build();

        GroqResponseDto groqResponseDto=HitTheGroq(groqRequestDto);
        return ExtractContent(groqResponseDto);
    }

    public String BuildContent(String content,String problem_title){
        StringBuilder content_for_Groq=new StringBuilder();
        content_for_Groq.append("Generate Notes for the given below Code along with correct approach \n");
        if(content!=null && content.trim().length()>0){
            content_for_Groq.append(content);
        }
        content_for_Groq.append("problem title from leetcode: "+problem_title+"\n");
        return content_for_Groq.toString();
    }

    public GroqResponseDto HitTheGroq(GroqRequestDto groqRequestDto){
        //hitting groq api
        GroqResponseDto groqResponseDto=webClient.post()
        .uri(Groq_api_url)
        .header("Authorization", "Bearer " + Groq_api_key) // for header auth
        .bodyValue(groqRequestDto)
        .retrieve()
        .bodyToMono(GroqResponseDto.class)
        .block();

        return groqResponseDto;
    }

    public String ExtractContent(GroqResponseDto groqResponseDto){

        return groqResponseDto.getChoices()
        .get(0)
        .getMessage()
        .getContent();
    }
}
