package com.example.promp_gpt.service;

import com.example.promp_gpt.entities.PromptResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OpenAiService {
    public PromptResponse getPrompt(String prompt) throws JsonProcessingException;
}
