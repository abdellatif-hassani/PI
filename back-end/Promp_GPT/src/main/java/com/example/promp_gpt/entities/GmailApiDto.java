package com.example.promp_gpt.entities;

import com.google.api.client.util.DateTime;
import lombok.*;

import java.util.List;

// EventDto class to represent necessary information for an event
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class GmailApiDto {
    private String from;
    private String to;
    private String subject;
    private String message;
    private List<Attachment> attachments;

}