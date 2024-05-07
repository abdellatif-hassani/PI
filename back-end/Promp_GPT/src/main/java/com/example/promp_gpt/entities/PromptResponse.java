package com.example.promp_gpt.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PromptResponse {
    private String typeAnswer;
    private String answerText;
    private GmailApiDto answerRelatedToGmail;
    private EventEntity answerRelatedToCalendar;
    private String methodToUse;
    private Boolean satisfied;
    private Boolean wantToCancel;
}
