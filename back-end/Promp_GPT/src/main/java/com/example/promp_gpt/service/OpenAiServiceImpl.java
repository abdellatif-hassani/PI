package com.example.promp_gpt.service;

import com.example.promp_gpt.entities.EventEntity;
import com.example.promp_gpt.entities.GmailApiDto;
import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.exception.SomeThingWentWrongException;
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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
    public Object sendToTheCorrectService(PromptResponse promptResponse,String token) throws SomeThingWentWrongException {

        if (promptResponse.getTypeAnswer().equals("email")) {
            GmailApiDto gmailApiDto = promptResponse.getAnswerRelatedToGmail();
            return sendToTheGemailService(gmailApiDto,promptResponse.getMethodToUse(),token);
        } else if (promptResponse.getTypeAnswer().equals("calendar")) {

            System.out.println(promptResponse.getAnswerRelatedToCalendar());
            EventEntity eventEntity = promptResponse.getAnswerRelatedToCalendar();
            if ( promptResponse.getAnswerRelatedToCalendar()==null || promptResponse.getAnswerRelatedToCalendar().getKeyword()==null)
                return sendToTheCalenderService(eventEntity,promptResponse.getMethodToUse(),token,null);
            return sendToTheCalenderService(eventEntity,promptResponse.getMethodToUse(),token,promptResponse.getAnswerRelatedToCalendar().getKeyword());
        } else if (promptResponse.getTypeAnswer().equals("message")) {
            return promptResponse.getAnswerText();
        }
        throw new SomeThingWentWrongException("Something went wrong");
    }


    @Override
    public GmailApiDto sendToTheGemailService(GmailApiDto gmailApiDto,String methodeToUse,String token) throws SomeThingWentWrongException {
            if (methodeToUse.equals("send")) {
                System.out.println("*****************************");
                gmailClient.sendEmail("Bearer " + token, gmailApiDto);
                return gmailApiDto;
            }
            throw new SomeThingWentWrongException("Something went wrong");
    }
    @Override
    public Object sendToTheCalenderService(EventEntity eventEntity,String methodeToUse,String token,String keyword) throws SomeThingWentWrongException {
            System.out.println("***********"+token);
        if (methodeToUse.equals("create")) {
            return calendarClient.setEvent("Bearer " + token, eventEntity);
        } else if (methodeToUse.equals("update")) {
            return calendarClient.updateEvent("Bearer " + token, eventEntity);
        } else if (methodeToUse.equals("delete")) {
            calendarClient.deleteEvent("Bearer " + token, eventEntity);
            return eventEntity;
        }
        else if (methodeToUse.equals("searchByKeyword")) {
           return calendarClient.searchEventsByKeyword("Bearer " + token, keyword);
        }
        else if (methodeToUse.equals("searchByDate")) {
            System.out.println("***********"+keyword);
           return calendarClient.searchEventsByDate("Bearer " + token, keyword);
        }
        else if (methodeToUse.equals("get")) {
            return calendarClient.getEvent("Bearer " + token);
        }
            throw new SomeThingWentWrongException("Something went wrong");

    }


}