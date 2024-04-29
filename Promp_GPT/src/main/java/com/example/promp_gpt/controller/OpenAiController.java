package com.example.promp_gpt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/openai")
public class OpenAiController {
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    private final ChatClient chatClient;

    public OpenAiController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/response")
    public ChatResponse getResponse(String message) throws JsonProcessingException {
        var openAiApi = new OpenAiApi(apiKey);

        var chatClient = new OpenAiChatClient(openAiApi, OpenAiChatOptions.builder()
                .withModel("gpt-3.5-turbo-0125")
                .withTemperature(0.4f)
                .build());

                String userText = """
          I want to send an email to abdelatif@gmail.com telling him that I will be in a meeting tomorrow at 10:00 AM.        """;

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
        //ObjectMapper objectMapper = new ObjectMapper();

// Assuming chatClient.call(prompt).getResult().getOutput().getContent() returns a JSON string
      //  String jsonResponse = chatClient.call(prompt).getResult().getOutput().getContent();

// Convert the JSON string to a Map
      //  Map<String, Object> response = objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});

        return chatClient.call(prompt);



    }
}
