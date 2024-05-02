package com.example.promp_gpt.controller;

import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.service.OpenAiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class OpenAiController {
    private final OpenAiService openAiService;

    public OpenAiController(ChatClient chatClient, OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @GetMapping("/response")
    public PromptResponse getResponse(String message, String toking) throws JsonProcessingException {
         PromptResponse promptResponse =openAiService.getPrompt(message);
        return promptResponse;
    }
    @GetMapping("/test")
    public String Response() {

            return "promptResponse";
    }
    @GetMapping("")
    public String hello() {

        return "hello";
    }

    @GetMapping("/prompt_gpt/test")
    public String helloo() {

        return "hello";
    }
}
