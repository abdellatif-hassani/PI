package pi.com.calendarservice.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "Web client 1";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
//    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    private GoogleAuthorizationCodeFlow flow;

//    public GoogleCalendarService() throws IOException, GeneralSecurityException {
//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        flow = new GoogleAuthorizationCodeFlow.Builder(
//                httpTransport, JSON_FACTORY, getClientSecrets(), SCOPES)
//                .build();
//    }
//
//    private GoogleClientSecrets getClientSecrets() throws IOException {
//        InputStream in = GoogleCalendarService.class.getResourceAsStream("/credentials.json");
//        return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//    }

    //getEvents method to get all events from the calendar
    public List<Event> getEvents(String accessToken) throws IOException, GeneralSecurityException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Events events = service.events().list("primary").execute();
        return events.getItems();
    }

    //addEvent method to add an event to the calendar
    public Event addEvent(String accessToken, Event eventToAdd) throws IOException, GeneralSecurityException {

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        return service.events().insert("primary", eventToAdd).execute();
    }

    //updateEvent method to update an event in the calendar
    public Event updateEvent(String accessToken, String eventId, Event updatedEvent) throws IOException, GeneralSecurityException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        return service.events().update("primary", eventId, updatedEvent).execute();
    }

    //searchEvent method to search for an event in the calendar
    public List<Event> searchEventsBySummary(String accessToken, String keyword) throws IOException, GeneralSecurityException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Set up the parameters for the event search
        Events events = service.events().list("primary")
                .setQ(keyword)  // Set the keyword for searching in the event summary
                .execute();

        return events.getItems();
    }


}

