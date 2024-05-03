package com.example.promp_gpt.controller;

import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.service.OpenAiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpHeaders;

@RestController
@RequestMapping("prompt_service")
public class OpenAiController {

    private final OpenAiService openAiService;

    public OpenAiController( OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @GetMapping("")
    public Object getResponse(String message, HttpServletRequest httpServletRequest) throws JsonProcessingException {
         String token=httpServletRequest.getHeader("Authorization");
         PromptResponse promptResponse =openAiService.getPrompt(message);

        return openAiService.sendToTheCorrectService(promptResponse,token);
    }

    @GetMapping("")
    public String hello() {

        return "hello";
    }


}
