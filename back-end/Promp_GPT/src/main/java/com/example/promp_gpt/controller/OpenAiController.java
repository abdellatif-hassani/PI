package com.example.promp_gpt.controller;

import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.prompt.PromptString;
import com.example.promp_gpt.service.OpenAiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("prompt_service")

public class OpenAiController {

    private final OpenAiService openAiService;

    public OpenAiController( OpenAiService openAiService) {
        this.openAiService = openAiService;
    }
    //for making the request to the model for the first time and getting the response
    @PostMapping("/prompt")
    public PromptResponse makePrompt(@RequestBody String message) throws JsonProcessingException {
        PromptResponse promptResponse =openAiService.getPrompt(message, PromptString.systemText_Prompt);
        System.out.println("****************"+promptResponse);
       return promptResponse;
    }
    @GetMapping("")
    public String get(){
        return "Hello World";
    }

    //for making the request to the model for reformulate the response
    @PostMapping("/reprompt")
    public PromptResponse makeRePrompt(@RequestBody PromptResponse promptResponse,@RequestBody String userText) throws JsonProcessingException {
         promptResponse =openAiService.getRePrompt(promptResponse,userText,PromptString.systemText_RePrompt);
         return promptResponse;
    }

    //for executing the final response
    @PostMapping("/exucute")
    public Object exucute(@RequestBody PromptResponse promptResponse, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String token="ya29.a0AXooCgugicUK128Manxp0DuUXeCOuCe1dzvAXaMJdFZGAOJyo9CgZNjMO3jxSu_GC28uWnFb6gn-dQQ8aAgHFff_Q3bDDNYS2U6C8FXbqmyZjxzjwlM84EgsacU2tZ9llkM0fXVpWRmWPDbKJshZPAnm94TpHOLV9PsaCgYKAdQSARMSFQHGX2MiqgA2Ehd99Rfvio80cZcWgw0170";
        return openAiService.sendToTheCorrectService(promptResponse,token);
    }

        @PostMapping("/test")
    public Object getPrompttest(@RequestBody String userText) throws JsonProcessingException {
    String apiKey="sk-proj-scD4dLR48YhjMN3Dt4JHT3BlbkFJaXkf9lyEDnGFaTKzn6xQ";

     OpenAiApi openAiApi = new OpenAiApi(apiKey);

     OpenAiChatClient chatClient = new OpenAiChatClient(openAiApi, OpenAiChatOptions.builder()
            .withModel("gpt-3.5-turbo")
            .withTemperature(0.4f)
            .build());


        Message userMessage = new UserMessage("");
        String systemText=PromptString.systemText_Prompt;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("userText", userText));
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        return chatClient.call(prompt).getResult();

}
}
