package com.example.promp_gpt.service;

import com.example.promp_gpt.entities.PromptResponse;
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

import java.util.List;
import java.util.Map;

public class OpenAiServiceImpl implements OpenAiService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    private final ChatClient chatClient;

    public OpenAiServiceImpl(ChatClient chatClient) {
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

        // Assuming you have the ObjectMapper initialized somewhere in your code
        ObjectMapper objectMapper = new ObjectMapper();

          // Assuming chatClient.call(prompt).getResult().getOutput().getContent() returns a JSON string

          String jsonResponse = chatClient.call(prompt).getResult().getOutput().getContent();
          // Convert the JSON string to a Map
         Map<String, Object> response = objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});

        return (PromptResponse)response;

    }




}