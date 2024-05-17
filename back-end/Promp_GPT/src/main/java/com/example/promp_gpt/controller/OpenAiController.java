package com.example.promp_gpt.controller;

import com.example.promp_gpt.entities.PromptResponse;
import com.example.promp_gpt.entities.RePromptRequest;
import com.example.promp_gpt.exception.SomeThingWentWrongException;
import com.example.promp_gpt.prompt.PromptString;
import com.example.promp_gpt.service.OpenAiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("")
public class OpenAiController {

    private final OpenAiService openAiService;

    public OpenAiController( OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/prompt")
    public Object makePrompt(@RequestBody String message,HttpServletRequest httpServletRequest) throws JsonProcessingException, SomeThingWentWrongException {
        String BearerToken = httpServletRequest.getHeader("Authorization");
        String token = BearerToken.substring(7);
        PromptResponse promptResponse =openAiService.getPrompt(message, PromptString.systemText_Prompt);
        System.out.println("****************"+promptResponse);
        System.out.println("************************************test********************************************");
        promptResponse.setSatisfied(false);
        promptResponse.setWantToCancel(false);
        if (promptResponse.getMethodToUse().equals("searchByKeyword") || promptResponse.getMethodToUse().equals("searchByDate") || promptResponse.getMethodToUse().equals("get")|| promptResponse.getMethodToUse().equals("delete"))
        {
           return openAiService.sendToTheCorrectService(promptResponse, token);
        }
        return promptResponse;
    }

    @PostMapping("/reprompt")
    public Object makeRePrompt(@RequestBody RePromptRequest rePromptRequest,HttpServletRequest httpServletRequest) throws JsonProcessingException, SomeThingWentWrongException {
        String BearerToken = httpServletRequest.getHeader("Authorization");
        String token = BearerToken.substring(7);
        if (rePromptRequest.getPromptResponse().getSatisfied()!=null && rePromptRequest.getPromptResponse().getSatisfied()==true) {
            return openAiService.sendToTheCorrectService(rePromptRequest.getPromptResponse(), token);

        } else {
            PromptResponse promptResponse = null;
            if (rePromptRequest.getPromptResponse().getTypeAnswer().equals("email"))
            {
                promptResponse= openAiService.getRePrompt(rePromptRequest.getPromptResponse(), rePromptRequest.getUserText(), PromptString.systemText_RePrompt_Gmail);
            }
            else if (rePromptRequest.getPromptResponse().getTypeAnswer().equals("calendar"))
            {
                promptResponse= openAiService.getRePrompt(rePromptRequest.getPromptResponse(), rePromptRequest.getUserText(), PromptString.systemText_RePrompt_Calendar);
                System.out.println("*********helllo"+promptResponse);
            }
            if (promptResponse!=null && promptResponse.getSatisfied()==true) {
                Object t=openAiService.sendToTheCorrectService(promptResponse, token);
                System.out.println(t);

                return openAiService.sendToTheCorrectService(promptResponse, token);

            }
            if (promptResponse!=null)
                return promptResponse;
            else
                throw new SomeThingWentWrongException("Something went wrong");

        }
    }
}