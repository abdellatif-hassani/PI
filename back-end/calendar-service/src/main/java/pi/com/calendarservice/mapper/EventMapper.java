package pi.com.calendarservice.mapper;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import pi.com.calendarservice.dto.EventDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// Mapper class to convert between Event and EventDto
public class EventMapper {

    // Method to convert from Event to EventDto
    public static EventDto toEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setSummary(event.getSummary());
        eventDto.setLocation(event.getLocation());
        eventDto.setDescription(event.getDescription());
        eventDto.setStartTime(event.getStart().getDateTime());
        eventDto.setEndTime(event.getEnd().getDateTime());
        return eventDto;
    }

    // Method to convert from EventDto to Event
    public static Event toEvent(EventDto eventDto) {
        Event event = new Event();
        event.setSummary(eventDto.getSummary());
        event.setLocation(eventDto.getLocation());
        event.setDescription(eventDto.getDescription());
        event.setStart(new com.google.api.services.calendar.model.EventDateTime()
                .setDateTime(eventDto.getStartTime()));
        event.setEnd(new com.google.api.services.calendar.model.EventDateTime()
                .setDateTime(eventDto.getEndTime()));
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
    private static String convertMillisToReadableDate(DateTime dateTime) {
        long millis = dateTime.getValue();
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
