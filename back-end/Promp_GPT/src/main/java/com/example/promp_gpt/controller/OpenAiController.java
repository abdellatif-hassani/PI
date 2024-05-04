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
       //  String token=httpServletRequest.getHeader("Authorization");
        String token="ya29.a0AXooCgtQYjvNdrBQ0ompdQfY4BilWjyDtdIWAF9BJJpnErqv9nyrxIid_Y2ECWqR8-TXMff4ZrxybNoTK0uqBHjfXG_uyJeykuYPjcshc5Ca2q4KQdGPS2s8gDyMeIfsMreq8dM1j0SGVx8JAQCon4_NflYypDhXKNUaCgYKAScSARMSFQHGX2Mik97kQQ-Llb6c2Aho3RG7eg0170";
        PromptResponse promptResponse =openAiService.getPrompt(message);
        System.out.println("****************"+promptResponse);
      // return openAiService.sendToTheCorrectService(promptResponse,token);
       return promptResponse;
    }

    @GetMapping("/hellpo")
    public String hello() {

        return "hello";
    }


}
