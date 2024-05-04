package com.example.gmailapi;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.gmail.Gmail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import entiites.AccountCredential;

import java.io.*;
import java.security.GeneralSecurityException;

import static com.google.api.client.json.gson.GsonFactory.getDefaultInstance;

@Configuration
public class GmailServiceConfig {

    private static final String APPLICATION_NAME = "VirtualAssistant";
    private static final JsonFactory JSON_FACTORY = getDefaultInstance();




    public Gmail gmailService(AccountCredential credential) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT,credential))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT,AccountCredential accountCredential) throws IOException {
        // Load client secrets

        // Build flow and trigger user authorization request.
         Credential credential = new Credential( new BearerToken().authorizationHeaderAccessMethod()).setAccessToken(accountCredential.getAccessToken());
        //returns an authorized Credential object.
        return credential;

    }

}
