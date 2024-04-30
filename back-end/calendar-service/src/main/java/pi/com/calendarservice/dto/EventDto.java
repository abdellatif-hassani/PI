package pi.com.calendarservice.dto;

import com.google.api.client.util.DateTime;
import lombok.*;

// EventDto class to represent necessary information for an event
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class EventDto {
    private String summary;
    private String location;
    private String description;
    private String startTime;
    private String endTime;

}