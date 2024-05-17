package com.example.promp_gpt.service;

import com.example.promp_gpt.entities.EventEntity;
import com.example.promp_gpt.entities.GmailApiDto;
import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.exception.SomeThingWentWrongException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OpenAiService {
    PromptResponse getPrompt(String userText, String systemText) throws JsonProcessingException;
    GmailApiDto sendToTheGemailService(GmailApiDto gmailApiDto, String methodeToUse, String token) throws SomeThingWentWrongException;
    Object sendToTheCalenderService(EventEntity eventEntity, String methodeToUse, String token,String keyword) throws SomeThingWentWrongException;
    Object sendToTheCorrectService(PromptResponse promptResponse, String token) throws SomeThingWentWrongException;
    PromptResponse getRePrompt(PromptResponse promptResponse,String userText,String systemText) throws JsonProcessingException;
}
