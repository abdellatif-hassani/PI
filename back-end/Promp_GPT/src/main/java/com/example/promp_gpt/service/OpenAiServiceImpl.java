package com.example.promp_gpt.service;

import com.example.promp_gpt.entities.EventEntity;
import com.example.promp_gpt.entities.GmailApiDto;
import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.service.clients.CalenderClient;
import com.example.promp_gpt.service.clients.GmailClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OpenAiServiceImpl implements OpenAiService {

    private final CalenderClient calendarClient;
    private final GmailClient gmailClient;


    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    private final ChatClient chatClient;

    public OpenAiServiceImpl(CalenderClient calendarClient, GmailClient gmailClient, ChatClient chatClient) {
        this.calendarClient = calendarClient;
        this.gmailClient = gmailClient;
        this.chatClient = chatClient;
    }

    @Override
    public PromptResponse getPrompt(String userText) throws JsonProcessingException {
        var openAiApi = new OpenAiApi(apiKey);

        var chatClient = new OpenAiChatClient(openAiApi, OpenAiChatOptions.builder()
                .withModel("gpt-3.5-turbo-0125")
                .withTemperature(0.4f)
                .build());

        Message userMessage = new UserMessage(userText);
        String systemText = """
                                      You are an AI assistant helping with email and task management.
                                      When the user specifies they want to send an email, you should generate a JSON object with the following structure:
                                        "type": "email or task",
                                        "content": "content of the message",
                                        "recipient": "recipient's email address"
                                      
                                      Ensure the JSON object is correctly formatted and includes the type (email or task), the content of the message, and the recipient's email address.
                                               
                        """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of());
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = chatClient.call(prompt).getResult().getOutput().getContent();
         Map<String, Object> response = objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});

        return (PromptResponse)response;

    }



    @Override
    public Object sendToTheCorrectService(PromptResponse promptResponse,String token) {
        if (promptResponse.getTypeAnswer().equals("email")) {
            GmailApiDto gmailApiDto = promptResponse.getAnswerRelatedToGmail();
            return sendToTheGemailService(gmailApiDto,promptResponse.getMathodeToUse(),token);
        } else if (promptResponse.getTypeAnswer().equals("calendar")) {
            EventEntity eventEntity = promptResponse.getAnswerRelatedToCalendar();
            return sendToTheCalenderService(eventEntity,promptResponse.getMathodeToUse(),token);
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
    public EventEntity sendToTheCalenderService(EventEntity eventEntity,String methodeToUse,String token) {
        if (methodeToUse.equals("create")) {
            calendarClient.setEvent("Bearer " + token, eventEntity);
        } else if (methodeToUse.equals("update")) {
            calendarClient.updateEvent("Bearer " + token, eventEntity);
        } else if (methodeToUse.equals("delete")) {
            calendarClient.deleteEvent("Bearer " + token, eventEntity);
        }
            return eventEntity;
    }


}