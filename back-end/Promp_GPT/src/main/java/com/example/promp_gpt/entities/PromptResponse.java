package com.example.promp_gpt.entities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class PromptResponse {
    private String typeAnswer;
    private String answerText;
    private GmailApiDto answerRelatedToGmail;
    private EventEntity answerRelatedToCalendar;
    private List<EventEntity> listEventsCalendar=new ArrayList<>();
    private String methodToUse;
    private Boolean satisfied;
    private Boolean wantToCancel;

    @Override
    public String toString() {
        return "PromptResponse{" +
                "typeAnswer='" + typeAnswer + '\'' +
                ", answerText='" + answerText + '\'' +
                ", answerRelatedToGmail=" + answerRelatedToGmail +
                ", answerRelatedToCalendar=" + answerRelatedToCalendar +
                ", methodToUse='" + methodToUse + '\'' +
                ", satisfied=" + satisfied +
                ", wantToCancel=" + wantToCancel +
                '}';
    }
}
