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

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("user_modifications", userText,"original_json_object",promptResponse.toString()));
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
            System.out.println(promptResponse);
            System.out.println(promptResponse.getAnswerRelatedToCalendar());
            EventEntity eventEntity = promptResponse.getAnswerRelatedToCalendar();

            if (promptResponse.getAnswerRelatedToCalendar()!=null) {
                if (promptResponse.getAnswerRelatedToCalendar().getKeyword() == null)
                    return sendToTheCalenderService(eventEntity, promptResponse.getMethodToUse(), token, null);
                else
                    return sendToTheCalenderService(eventEntity, promptResponse.getMethodToUse(), token, promptResponse.getAnswerRelatedToCalendar().getKeyword());
            }else
                return sendToTheCalenderService(eventEntity, promptResponse.getMethodToUse(), token, null);

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
    public Object sendToTheCalenderService(EventEntity eventEntity,String methodToUse,String token,String keyword) throws SomeThingWentWrongException{


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            DateTimeFormatter formatterIso = DateTimeFormatter.ISO_OFFSET_DATE_TIME;



            String now = dateFormat.format(new Date().getTime());



            Object execute = null;

            if (methodToUse.equals("create")) {
                if (eventEntity.getStartTime() == null) {
                    eventEntity.setStartTime(now + ":00+01:00");
                }
                if (eventEntity.getEndTime() == null) {
                    OffsetDateTime offsetDateTime = OffsetDateTime.parse(eventEntity.getStartTime() , formatterIso);
                    LocalDate localDate = offsetDateTime.toLocalDate();
                    LocalDateTime localDateTime = localDate.atStartOfDay();

                    // Add 24 hours to the LocalDateTime
                     localDateTime = localDateTime.plusHours(24);

                    // Convert LocalDateTime to Date
                    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
                    Date date = Date.from(zonedDateTime.toInstant());

                    // Format the Date object
                    String end = dateFormat.format(date);
                    eventEntity.setEndTime(end+":00+01:00");
                }

                execute = calendarClient.setEvent("Bearer " + token, eventEntity);
            } else if (methodToUse.equals("deleteByKeyword")) {
                return calendarClient.deleteEventBySummary("Bearer " + token, eventEntity.getKeyword());

            } else if (methodToUse.equals("deleteByDate")) {
                return calendarClient.deleteEventByDate("Bearer " + token, eventEntity.getKeyword());
            }
            else if (methodToUse.equals("searchByKeyword")) {
                 execute = calendarClient.searchEventsByKeyword("Bearer " + token, keyword);

            } else if (methodToUse.equals("searchByDate")) {

                execute=calendarClient.searchEventsByDate("Bearer " + token, keyword);
            } else if (methodToUse.equals("get")) {
                execute= calendarClient.getEvent("Bearer " + token);
            }
        if (execute==null)
            throw new SomeThingWentWrongException("Something went wrong");
        return execute;
        }


}