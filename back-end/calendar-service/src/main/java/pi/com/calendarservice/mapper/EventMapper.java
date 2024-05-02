package pi.com.calendarservice.mapper;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pi.com.calendarservice.dto.EventDto;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;


// Mapper class to convert between Event and EventDto
@Service
public class EventMapper {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    // Method to convert from Event to EventDto
    public static EventDto toEventDto(Event event) {
        EventDto eventDto = new EventDto();
        // Copy properties from Event to EventDto without BeanUtils
        eventDto.setSummary(event.getSummary());
        eventDto.setLocation(event.getLocation());
        eventDto.setDescription(event.getDescription());
        eventDto.setStartTime(convertMillisToReadableDate(event.getStart().getDateTime().getValue()));
        eventDto.setEndTime(convertMillisToReadableDate(event.getEnd().getDateTime().getValue()));
        System.out.println(eventDto.getStartTime() + " " + eventDto.getEndTime());
        return eventDto;
    }

    // Method to convert from EventDto to Event
    public static Event toEvent(EventDto eventDto) {
        Event event = new Event();
        BeanUtils.copyProperties(eventDto, event);
        // Convert start time string to DateTime
        DateTime startDateTime = new DateTime(eventDto.getStartTime());
        EventDateTime eventStart = new EventDateTime().setDateTime(startDateTime);
        event.setStart(eventStart);
        // Convert end time string to DateTime
        DateTime endDateTime = new DateTime(eventDto.getEndTime());
        EventDateTime eventEnd = new EventDateTime().setDateTime(endDateTime);
        event.setEnd(eventEnd);

        return event;
    }

    // Method to convert a list of Events to a list of EventDtos
    public static List<EventDto> toEventDtos(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }
    // Method to convert a list of EventDtos to a list of Events
    public static List<Event> toEvents(List<EventDto> eventDtos) {
        return eventDtos.stream()
                .map(EventMapper::toEvent)
                .collect(Collectors.toList());
    }

    // Method to convert milliseconds to readable date
    private static String convertMillisToReadableDate(long millis) {
        DateTime dateTime = new DateTime(millis);
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC")); // Set timezone to UTC to remove offset
        return DATE_FORMAT.format(dateTime.getValue());
    }
}
