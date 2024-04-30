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

    private GoogleCalendarService calendarService;

    public CalendarController(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    public static String accessToken = "ya29.a0Ad52N3-F5CpIpuS4Fg1FaRrawBtnCA5yLGxudG98Ejj0k_CKFxFH1LvQ-PgD9YzT1r9HMompa-mc4LuHqkBC2asGX2mg9YNTRxMg6eTCXVOL9hQTU0g2Z8v7xB1hksL-Tpzfwl18yIYwcVb-9j9lWfth-AMw9MyFJxBaBwaCgYKASsSARMSFQHGX2Mi6TFyCoyRtfvYRcnbNvvw2A0173";

    @GetMapping("/events")
    public List<EventDto> getEvents() throws IOException, GeneralSecurityException {
        // Assuming you have the access token stored in the session or database
        return calendarService.getEvents(accessToken);
    }

    @PostMapping("/events")
    public EventDto addEvent(@RequestBody EventDto eventDto) throws IOException, GeneralSecurityException {
        // Assuming you have the access token stored in the session or database
        return calendarService.addEvent(accessToken, eventDto);
    }

    //Just for testing
    @GetMapping("/addEvent")
    public EventDto addEvent() throws IOException, GeneralSecurityException {
        // Example event to add
        Event eventToAdd = new Event();
        eventToAdd.setSummary("Presentation about PI");
        eventToAdd.setLocation("Conference Room");
        eventToAdd.setDescription("Weekly team meeting to discuss progress and tasks.");
        DateTime startDateTime = new DateTime("2024-04-30T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime);
        eventToAdd.setStart(start);
        DateTime endDateTime = new DateTime("2024-04-30T10:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime);
        eventToAdd.setEnd(end);
        // Assuming you have the access token stored in the session or database
        return calendarService.addEvent(accessToken, EventMapper.toEventDto(eventToAdd));
    }

    //updateEvent method to update an event in the calendar
    @GetMapping("/updateEvent")
    public EventDto updateEvent() throws IOException, GeneralSecurityException {
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
        EventDto eventDto = EventMapper.toEventDto(eventToUpdate);
        System.out.println("Event to update: " + eventDto.getStartTime());
        return calendarService.updateEvent(accessToken, eventId, eventDto);
    }

    @GetMapping("/searchEvent")
    public List<EventDto> searchEventsBySummary() throws IOException, GeneralSecurityException {
        String keyword = "Conference about AI";
        return calendarService.searchEventsBySummary(accessToken, keyword);
    }







}
