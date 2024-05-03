package com.example.promp_gpt.entities;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PromptResponse {
    private TypeAnswer typeAnswer;
    private String answerText;
    private GmailApiDto answerRelatedToGmail;
    private EventEntity answerRelatedToCalendar;
    private String mathodeToUse;
}
