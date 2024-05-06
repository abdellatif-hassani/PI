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
        String token="ya29.a0AXooCgugicUK128Manxp0DuUXeCOuCe1dzvAXaMJdFZGAOJyo9CgZNjMO3jxSu_GC28uWnFb6gn-dQQ8aAgHFff_Q3bDDNYS2U6C8FXbqmyZjxzjwlM84EgsacU2tZ9llkM0fXVpWRmWPDbKJshZPAnm94TpHOLV9PsaCgYKAdQSARMSFQHGX2MiqgA2Ehd99Rfvio80cZcWgw0170";
        PromptResponse promptResponse =openAiService.getPrompt(message);
        System.out.println("****************"+promptResponse);
       return openAiService.sendToTheCorrectService(promptResponse,token);
      // return promptResponse;
    }




}
