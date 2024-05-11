package com.example.speechtotextservice.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpeechToTextService {
    private final String apiKey = "YOUR_ASSEMBLYAI_API_KEY";
    private final String endpoint = "https://api.assemblyai.com/v2/transcript";

    public String transcribeAudio(String audioUrl) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", apiKey);

        String requestBody = "{\"audio_url\": \"" + audioUrl + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(endpoint, entity, String.class);

        return response.getBody();
    }
}
