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

    private final CalenderClient calendarClient;
    private final GmailClient gmailClient;


    @Value("${spring.ai.openai.api-key}")
    private String apiKey;


    public OpenAiServiceImpl(CalenderClient calendarClient, GmailClient gmailClient) {
        this.calendarClient = calendarClient;
        this.gmailClient = gmailClient;

    }

    @Override
    public PromptResponse getPrompt(String userText) throws JsonProcessingException {
        var openAiApi = new OpenAiApi(apiKey);

        var chatClient = new OpenAiChatClient(openAiApi, OpenAiChatOptions.builder()
                .withModel("gpt-3.5-turbo")
                .withTemperature(0.4f)
                .build());

        Message userMessage = new UserMessage("");
        String systemText = """
    we are building an chat application when the user asks to interact with his gmail like sending email for him or adding event to his calender or deleting or updating an event on the his calender or the user just ask a normal question the application should replay to it. our service receives user input and needs to generate responses based on the input.

    Our service should be able to handle three types of responses:
    1. If the user input is related to sending an email, the service should respond with the necessary information to send the email, including the sender, recipient, subject, message, and any attachments.
    2. If the user input is related to creating, updating, or deleting a calendar event, the service should respond with the necessary information for the calendar event, including the summary, location, description, start time, and end time.
    3. If the user input does not require interaction with email or calendar APIs, the service should simply return the text of the response message.

    Your task is to generate a JSON object representing the response based on the user input. The JSON object should have the following structure:

    - If the `typeAnswer` is `email`, then `answerText` and `answerRelatedToGmail` should not exist.
    - If the `typeAnswer` is `calendar`, then `answerText` and `answerRelatedToCalendar` should not exist.
    - If the `typeAnswer` is `message`, then `answerRelatedToGmail` and `answerRelatedToCalendar` should not exist.
    here is the object response:
      "typeAnswer": "email or calendar or message",
      "answerText": "answer what the user input is related to message"
      "answerRelatedToGmail": 
            "to": "string",
            "subject": "string",
            "message": "string",
            "attachments": 
                  "name": "string",
                  "url": "string"
        
      ,
      "answerRelatedToCalendar": 
            "summary": "string",
            "location": "string",
            "description": "string",
            "startTime": "you need to respect this format: YYYY-MM-DDTHH:mm+01:00 for example 2024-04-29T17:00:00+01:00",
            "endTime": "you need to respect this format: YYYY-MM-DDTHH:mm+01:00 for example 2024-04-29T17:00:00+01:00"
      ,
      "methodToUse": "send or get or create or update or delete"
     
     finally don't add any think that the user didn't ask for it.if there is no attachments don't mention it in the response.
     
     now here is the user input:
     {userText}
    """;


        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("userText", userText));
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        ObjectMapper objectMapper = new ObjectMapper();
         String jsonResponse = chatClient.call(prompt).getResult().getOutput().getContent();
        PromptResponse response = objectMapper.readValue(jsonResponse, new TypeReference<PromptResponse>() {});

        return response;

    }



    @Override
    public Object sendToTheCorrectService(PromptResponse promptResponse,String token) {
        System.out.println("hello1"+promptResponse.getTypeAnswer()+promptResponse.getTypeAnswer().equals("calendar"));
        if (promptResponse.getTypeAnswer().equals("email")) {
            GmailApiDto gmailApiDto = promptResponse.getAnswerRelatedToGmail();
            return sendToTheGemailService(gmailApiDto,promptResponse.getMethodToUse(),token);
        } else if (promptResponse.getTypeAnswer().equals("calendar")) {
            System.out.println("hello2");
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
            } else if (methodeToUse.equals("draft")) {

            }
            gmailClient.sendEmail("Bearer " + token, gmailApiDto);
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