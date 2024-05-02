package pi.com.calendarservice.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import pi.com.calendarservice.dto.EventDto;
import pi.com.calendarservice.service.GoogleCalendarService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/events")
public class CalendarController {

    private GoogleCalendarService calendarService;

    public CalendarController(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("")
    public List<EventDto> getEvents(HttpServletRequest request) throws IOException, GeneralSecurityException {
        String accessToken = (String) request.getAttribute("accessToken");
        return calendarService.getEvents(accessToken);
    }

    @PostMapping("/add")
    public EventDto addEvent(@RequestBody EventDto eventDto,HttpServletRequest request) throws IOException, GeneralSecurityException {
        String accessToken = (String) request.getAttribute("accessToken");
        return calendarService.addEvent(accessToken, eventDto);
    }

    @GetMapping("/search")
    public List<EventDto> searchEventsBySummary(@RequestParam String keyword, HttpServletRequest request) throws IOException, GeneralSecurityException {
        String accessToken = (String) request.getAttribute("accessToken");
        return calendarService.searchEventsBySummary(accessToken, keyword);
    }

    //deleteEvent method to delete an event from the calendar
    @DeleteMapping("/delete")
    public void deleteEvent(@RequestParam String eventSummary,  HttpServletRequest request) throws IOException, GeneralSecurityException {
        String accessToken = (String) request.getAttribute("accessToken");
        calendarService.deleteEvent(accessToken, eventSummary);
    }

    @PutMapping("/update")
    public EventDto updateEvent(@RequestParam String eventId, @RequestBody EventDto updatedEvent, HttpServletRequest request) throws IOException, GeneralSecurityException {
        String accessToken = (String) request.getAttribute("accessToken");
        return calendarService.updateEvent(accessToken, eventId, updatedEvent);
    }






}
