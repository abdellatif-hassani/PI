package com.example.promp_gpt.service;

import com.example.promp_gpt.entities.EventEntity;
import com.example.promp_gpt.entities.GmailApiDto;
import com.example.promp_gpt.entities.PromptResponse;
import com.fasterxml.jackson.core.JsonProcessingException;



public interface OpenAiService {
   PromptResponse getPrompt(String userText,String systemText) throws JsonProcessingException;
   GmailApiDto sendToTheGemailService(GmailApiDto gmailApiDto, String methodeToUse, String token);
    Object sendToTheCalenderService(EventEntity eventEntity, String methodeToUse, String token);



    Object sendToTheCorrectService(PromptResponse promptResponse, String token);

    PromptResponse getRePrompt(PromptResponse promptResponse,String userText,String systemText) throws JsonProcessingException;
}
