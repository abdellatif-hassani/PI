package pi.com.calendarservice.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pi.com.calendarservice.dto.EventDto;
import pi.com.calendarservice.service.GoogleCalendarService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/events")
//allow requests from any origin
//@CrossOrigin(origins = "*")
public class CalendarController {
    private final GoogleCalendarService calendarService;
    public CalendarController(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("")
    public List<EventDto> getEvents(HttpServletRequest request) throws IOException, GeneralSecurityException {
        String accessToken = (String) request.getAttribute("accessToken");
        System.out.println("Access token: " + accessToken);
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

    //updateEvent method to update an event in the calendar
    @PutMapping("/update")
    public EventDto updateEvent(@RequestParam String eventSummary, @RequestBody EventDto updatedEvent, HttpServletRequest request) throws IOException, GeneralSecurityException {
        String accessToken = (String) request.getAttribute("accessToken");
        return calendarService.updateEvent(accessToken, eventSummary, updatedEvent);
    }
    @GetMapping("/searchByDate")
    public List<EventDto> searchEventsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                             HttpServletRequest request) throws IOException, GeneralSecurityException {
        System.out.println("Date: " + date);
        String accessToken = (String) request.getAttribute("accessToken");
        return calendarService.searchEventsByDate(accessToken, date);
    }






}
