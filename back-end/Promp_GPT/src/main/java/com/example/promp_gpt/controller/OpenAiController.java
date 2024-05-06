package com.example.promp_gpt.controller;

import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.service.OpenAiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.ChatClient;
import org.springframework.http.ResponseEntity;
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
        String token="ya29.a0AXooCgvP5fNFyqOu7ddazufv8BBCusuQRnKZb-Vh2BaTHxyDdhEb8YPsVrlobbOHjqhPJ-6UYY26nkcZBIWs7rP1ypdPiJdjsAZP9P6U4QxO7W4PcTpeWyBiIJGutpHLDhwON6zpGMEyG4s0riHYDh2cdMe8Kl_VAQaCgYKAVkSARASFQHGX2MiWM_h_5_wk_7FFjGpDhVNwg0169";
        PromptResponse promptResponse =openAiService.getPrompt(message);
        System.out.println("****************"+promptResponse);
       return openAiService.sendToTheCorrectService(promptResponse,token);
      // return promptResponse;
    }

    @GetMapping("/api")
    public ResponseEntity<String> answer(String message, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String token="ya29.a0AXooCgvP5fNFyqOu7ddazufv8BBCusuQRnKZb-Vh2BaTHxyDdhEb8YPsVrlobbOHjqhPJ-6UYY26nkcZBIWs7rP1ypdPiJdjsAZP9P6U4QxO7W4PcTpeWyBiIJGutpHLDhwON6zpGMEyG4s0riHYDh2cdMe8Kl_VAQaCgYKAVkSARASFQHGX2MiWM_h_5_wk_7FFjGpDhVNwg0169";
        PromptResponse promptResponse = openAiService.getPrompt(message);
        System.out.println("****************"+promptResponse);

        // Assume openAiService.sendToTheCorrectService returns a string, convert it to JSON
        String jsonResponse = "{\"response\": \"" + openAiService.sendToTheCorrectService(promptResponse, token) + "\"}";

        return ResponseEntity.ok().body(jsonResponse);
    }




}
