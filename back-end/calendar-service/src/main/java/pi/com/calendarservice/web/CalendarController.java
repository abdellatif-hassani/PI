package pi.com.calendarservice.web;

import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pi.com.calendarservice.service.GoogleCalendarService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
public class CalendarController {

    @Autowired
    private GoogleCalendarService calendarService;

    @GetMapping("/events")
    public List<Event> getEvents() throws IOException, GeneralSecurityException {
        // Assuming you have the access token stored in the session or database
        String accessToken = "ya29.a0Ad52N3_wYYw4ICnP5d2WMpXY1eYbGpqMJ8tRA68rXec-fzR4yPK57N_X92d4vQAaeOTMATbtU7bW9bj4urUxIA3agoYTGXeN4GoJADadltluuhF0DVrc7uMS_njmKhWWP4XbbLflvhncWBww6FbPHsMPmN6hckaFw8VeaCgYKAUwSARMSFQHGX2Mi6ac4sA-wiMNst7qnUQiWwg0171";
        return calendarService.getEvents(accessToken);
    }
}
