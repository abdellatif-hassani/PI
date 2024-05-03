package com.example.promp_gpt.entities;

import com.google.api.client.util.DateTime;
import lombok.*;

// EventDto class to represent necessary information for an event
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class EventEntity {
    private String id;
    private String summary;
    private String location;
    private String description;
    private String startTime;
    private String endTime;

}