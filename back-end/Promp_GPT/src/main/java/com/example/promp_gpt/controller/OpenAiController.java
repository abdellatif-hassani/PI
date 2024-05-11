package com.example.promp_gpt.controller;

import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.entities.RePromptRequest;
import com.example.promp_gpt.exception.SomeThingWentWrongException;
import com.example.promp_gpt.prompt.PromptString;
import com.example.promp_gpt.service.OpenAiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


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
        if (rePromptRequest.getPromptResponse().getSatisfied()!=null && rePromptRequest.getPromptResponse().getSatisfied()==true) {
            openAiService.sendToTheCorrectService(rePromptRequest.getPromptResponse(), token);
            return rePromptRequest.getPromptResponse();
        } else {
            PromptResponse promptResponse = openAiService.getRePrompt(rePromptRequest.getPromptResponse(), rePromptRequest.getUserText(), PromptString.systemText_RePrompt);
            if (promptResponse.getSatisfied()==true) openAiService.sendToTheCorrectService(promptResponse, token);
            return promptResponse;
        }
    }
}