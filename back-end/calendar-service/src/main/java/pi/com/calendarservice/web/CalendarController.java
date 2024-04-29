package pi.com.calendarservice.web;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pi.com.calendarservice.dto.EventDto;
import pi.com.calendarservice.mapper.EventMapper;
import pi.com.calendarservice.service.GoogleCalendarService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
public class CalendarController {

    @Autowired
    private GoogleCalendarService calendarService;
    public static String accessToken = "ya29.a0Ad52N38YnFb3BAu7hhuu2U4qqvMqUssqGHJx58Gnh7hBNcRgni50-zJ5cU6RWntJHqiTe9C77S2yB-gG8ax36aYg7a4ALXXzPxu2YHUxHOxa3djh0empbOyAjn5aZg_Dg_bdyclptr6lbwnxcZMb6DwWmJcGF2bJnLpCaCgYKAeoSARMSFQHGX2Mi9ugVBZXO1K3mF50453k_bA0171";

    @GetMapping("/events")
    public List<EventDto> getEvents() throws IOException, GeneralSecurityException {
        // Assuming you have the access token stored in the session or database
        return EventMapper.toEventDtos(calendarService.getEvents(accessToken));
    }

    @PostMapping("/events")
    public Event addEvent(@RequestBody EventDto eventDto) throws IOException, GeneralSecurityException {
        // Convert EventDto to Event
        Event eventToAdd = EventMapper.toEvent(eventDto);
        // Assuming you have the access token stored in the session or database
        return calendarService.addEvent(accessToken, eventToAdd);
    }

    @GetMapping("/addEvent")
    public Event addEvent() throws IOException, GeneralSecurityException {
        // Example event to add
        Event eventToAdd = new Event();
        eventToAdd.setSummary("ENSET Mohammedia");
        eventToAdd.setLocation("Conference Room");
        eventToAdd.setDescription("Weekly team meeting to discuss progress and tasks.");
        DateTime startDateTime = new DateTime("2024-05-01T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime);
        eventToAdd.setStart(start);
        DateTime endDateTime = new DateTime("2024-05-01T10:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime);
                //.setTimeZone("America/Los_Angeles")
        eventToAdd.setEnd(end);
        // Assuming you have the access token stored in the session or database
        return calendarService.addEvent(accessToken, eventToAdd);
    }

    //updateEvent method to update an event in the calendar
    @GetMapping("/updateEvent")
    public Event updateEvent() throws IOException, GeneralSecurityException {
        // Example event to update
        String eventId = "1bcs9a69le9b98lgd80l054kva"; //
        Event eventToUpdate = new Event();
        eventToUpdate.setSummary("Conference about AI");
        eventToUpdate.setLocation("Conference Room Updated");
        eventToUpdate.setDescription("Weekly team meeting to discuss progress and tasks. Updated.");
        DateTime startDateTime = new DateTime("2024-05-01T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        eventToUpdate.setStart(start);
        DateTime endDateTime = new DateTime("2024-05-01T10:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        eventToUpdate.setEnd(end);
        return calendarService.updateEvent(accessToken, eventId, eventToUpdate);
    }

    @GetMapping("/searchEvent")
    public List<Event> searchEventsBySummary() throws IOException, GeneralSecurityException {
        String keyword = "Conference about AI";
        return calendarService.searchEventsBySummary(accessToken, keyword);
    }







}
