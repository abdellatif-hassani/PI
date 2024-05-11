package com.example.speechtotextservice.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

@Configuration
public class GoogleSpeechConfig {

    @Value("${google.cloud.credentials}")
    private String googleCredentials;

    @Bean
    public SpeechClient speechClient() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource(googleCredentials).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        SpeechSettings settings = SpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        return SpeechClient.create(settings);
    }
}
