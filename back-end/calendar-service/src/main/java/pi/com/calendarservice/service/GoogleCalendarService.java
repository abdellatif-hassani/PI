package pi.com.calendarservice.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "Web client 1";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

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

    public List<Event> getEvents(String accessToken) throws IOException, GeneralSecurityException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Events events = service.events().list("primary").execute();
        return events.getItems();
    }

}

