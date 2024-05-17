package pi.com.calendarservice.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.stereotype.Service;
import pi.com.calendarservice.dto.EventDto;
import pi.com.calendarservice.mapper.EventMapper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class GoogleCalendarService {
    private final EventMapper eventMapper;

    public GoogleCalendarService(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    private static final String APPLICATION_NAME = "Web client 1";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    //getEvents method to get all events from the calendar
    public List<EventDto> getEvents(String accessToken) throws IOException, GeneralSecurityException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Events events = service.events().list("primary").execute();
        return EventMapper.toEventDtos(events.getItems());
    }

    //addEvent method to add an event to the calendar
    public EventDto addEvent(String accessToken, EventDto eventDto) throws IOException, GeneralSecurityException {
        // Convert EventDto to Event
        Event eventToAdd = EventMapper.toEvent(eventDto);
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        Object execute = service.events().insert("primary", eventToAdd).execute();
        return EventMapper.toEventDto((Event) execute);
    }

    //updateEvent method to update an event in the calendar
    public EventDto updateEvent(String accessToken, String eventSummary, EventDto updatedEvent) throws IOException, GeneralSecurityException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        List<EventDto> eventDtos = searchEventsBySummary(accessToken, eventSummary);
        EventDto eventDto = eventDtos.get(0);
        Event eventToUpdate = EventMapper.toEvent(updatedEvent);
        Event event =  service.events().update("primary", eventDto.getId(), eventToUpdate).execute();
        return EventMapper.toEventDto(event);
    }

    //searchEvent method to search for an event in the calendar
    public List<EventDto> searchEventsBySummary(String accessToken, String keyword) throws IOException, GeneralSecurityException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Define the query parameters for searching events
        Events events = service.events().list("primary")
                .setQ(keyword)
                .execute();
        return EventMapper.toEventDtos(events.getItems());
    }


    public Object deleteEvent(String accessToken, String keyword) {
        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

            Calendar service = new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            List<EventDto> eventDtos = searchEventsBySummary(accessToken, keyword);
            if(!eventDtos.isEmpty()) {
                String eventId = eventDtos.get(0).getId();
                return service.events().delete("primary", eventId).execute();
            }
            else {
                System.out.println("Event not found");
            }
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<EventDto> searchEventsByDate(String accessToken, LocalDate date) throws IOException, GeneralSecurityException {
        // Convert LocalDate to Date for API query
        Date startDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(date.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("Start date: " + startDate);
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Define the query parameters for searching events
        Events events = service.events().list("primary")
                .setTimeMin(new DateTime(startDate))
                .setTimeMax(new DateTime(endDate))
                .execute();

        return EventMapper.toEventDtos(events.getItems());
    }




}

