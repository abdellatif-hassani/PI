package com.example.promp_gpt.service;

import com.example.promp_gpt.entities.EventEntity;
import com.example.promp_gpt.entities.GmailApiDto;
import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.service.clients.CalenderClient;
import com.example.promp_gpt.service.clients.GmailClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ai.chat.messages.Message;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiServiceImpl implements OpenAiService {
    //@Value("${spring.ai.openai.api-key}")
    private final String apiKey="sk-proj-scD4dLR48YhjMN3Dt4JHT3BlbkFJaXkf9lyEDnGFaTKzn6xQ";

    private final OpenAiApi openAiApi = new OpenAiApi(apiKey);

    private final OpenAiChatClient chatClient = new OpenAiChatClient(openAiApi, OpenAiChatOptions.builder()
            .withModel("gpt-3.5-turbo")
            .withTemperature(0.4f)
            .build());

    private final CalenderClient calendarClient;
    private final GmailClient gmailClient;



    public OpenAiServiceImpl(CalenderClient calendarClient, GmailClient gmailClient) {
        this.calendarClient = calendarClient;
        this.gmailClient = gmailClient;

    }
    @Override
    public PromptResponse getPrompt(String userText, String systemText) throws JsonProcessingException {

        Message userMessage = new UserMessage("");

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("userText", userText));
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        ObjectMapper objectMapper = new ObjectMapper();
         String jsonResponse = chatClient.call(prompt).getResult().getOutput().getContent();
        PromptResponse response = objectMapper.readValue(jsonResponse, new TypeReference<PromptResponse>() {});

        return response;

    }

    @Override
    public PromptResponse getRePrompt(PromptResponse promptResponse, String userText, String systemText) throws JsonProcessingException {

        Message userMessage = new UserMessage("");
        System.out.println("******"+promptResponse);

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("user_modifications", userText,"original_json_object",promptResponse));
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(systemMessage);
        String jsonResponse = chatClient.call(prompt).getResult().getOutput().getContent();
        PromptResponse response = objectMapper.readValue(jsonResponse, new TypeReference<PromptResponse>() {});

        return response;
    }

    @Override
    public Object sendToTheCorrectService(PromptResponse promptResponse,String token) {

        if (promptResponse.getTypeAnswer().equals("email")) {
            GmailApiDto gmailApiDto = promptResponse.getAnswerRelatedToGmail();
            return sendToTheGemailService(gmailApiDto,promptResponse.getMethodToUse(),token);
        } else if (promptResponse.getTypeAnswer().equals("calendar")) {

            System.out.println(promptResponse.getAnswerRelatedToCalendar());
            EventEntity eventEntity = promptResponse.getAnswerRelatedToCalendar();

            return sendToTheCalenderService(eventEntity,promptResponse.getMethodToUse(),token);
        } else if (promptResponse.getTypeAnswer().equals("message")) {
            return promptResponse.getAnswerText();
        }
        return null;
    }


    @Override
    public GmailApiDto sendToTheGemailService(GmailApiDto gmailApiDto,String methodeToUse,String token) {
            if (methodeToUse.equals("send")) {
                gmailClient.sendEmail("Bearer " + token, gmailApiDto);
            }
            return gmailApiDto;
    }
    @Override
    public Object sendToTheCalenderService(EventEntity eventEntity,String methodeToUse,String token) {
        if (methodeToUse.equals("create")) {
            return calendarClient.setEvent("Bearer " + token, eventEntity);
        } else if (methodeToUse.equals("update")) {
            return calendarClient.updateEvent("Bearer " + token, eventEntity);
        } else if (methodeToUse.equals("delete")) {
            calendarClient.deleteEvent("Bearer " + token, eventEntity);
        }
        else if (methodeToUse.equals("get")) {
            return calendarClient.getEvent("Bearer " + token);
        }
            return eventEntity;
    }


}