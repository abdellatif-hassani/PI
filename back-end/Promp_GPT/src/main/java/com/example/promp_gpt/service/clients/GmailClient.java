package com.example.promp_gpt.service.clients;

import com.example.promp_gpt.entities.EventEntity;
import com.example.promp_gpt.entities.GmailApiDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "gmail-service")
public interface GmailClient {
    @PostMapping("/send")
    List<EventEntity> sendEmail(@RequestHeader("Authorization") String authorizationHeader, @RequestBody GmailApiDto gmailApiDto);
}
