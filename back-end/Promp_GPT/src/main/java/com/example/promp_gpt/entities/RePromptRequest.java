package com.example.promp_gpt.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RePromptRequest {
    private PromptResponse promptResponse;
    private String userText;

    // Getters and setters
}
