package com.example.promp_gpt.controller;

import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.entities.RePromptRequest;
import com.example.promp_gpt.exception.SomeThingWentWrongException;
import com.example.promp_gpt.prompt.PromptString;
import com.example.promp_gpt.service.OpenAiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("")


public class OpenAiController {

    private final OpenAiService openAiService;

    public OpenAiController( OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/prompt")
    public PromptResponse makePrompt(@RequestBody String message) throws JsonProcessingException {
        PromptResponse promptResponse =openAiService.getPrompt(message, PromptString.systemText_Prompt);
        System.out.println("****************"+promptResponse);
        return promptResponse;
    }

    @PostMapping("/reprompt")
    public PromptResponse makeRePrompt(@RequestBody RePromptRequest rePromptRequest,HttpServletRequest httpServletRequest) throws JsonProcessingException, SomeThingWentWrongException {
        String BearerToken = httpServletRequest.getHeader("Authorization");
        String token = BearerToken.substring(7);
        if (rePromptRequest.getPromptResponse().getSatisfied()) {
            openAiService.sendToTheCorrectService(rePromptRequest.getPromptResponse(), token);
            return rePromptRequest.getPromptResponse();
        } else {
            PromptResponse promptResponse = openAiService.getRePrompt(rePromptRequest.getPromptResponse(), rePromptRequest.getUserText(), PromptString.systemText_RePrompt);
            if (promptResponse.getSatisfied()) openAiService.sendToTheCorrectService(promptResponse, token);
            return promptResponse;
        }
    }
}