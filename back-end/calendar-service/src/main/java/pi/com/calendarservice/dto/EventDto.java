package pi.com.calendarservice.dto;

import lombok.*;

// EventDto class to represent necessary information for an event
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder @ToString
public class EventDto {
    private String id;
    private String summary;
    private String location;
    private String description;
    private String startTime;
    private String endTime;

}