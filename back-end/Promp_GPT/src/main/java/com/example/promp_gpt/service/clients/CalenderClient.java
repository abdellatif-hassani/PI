package com.example.promp_gpt.service.clients;


import com.example.promp_gpt.entities.EventEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;


@FeignClient(name = "calendar-service")
public interface CalenderClient {
    @GetMapping("/events")
    List<EventEntity> getEvent(@RequestHeader("Authorization") String authorizationHeader);
    @PostMapping("/events/add")
        EventEntity setEvent(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EventEntity event);
    @PutMapping ("/events/update")
        EventEntity updateEvent(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EventEntity event);
    @DeleteMapping ("/events/delete")
    Object deleteEvent(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String eventSummary);
    @GetMapping("/events/search")
    List<EventEntity> searchEventsByKeyword(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("keyword") String keyword);
    @GetMapping("/events/searchByDate")
    List<EventEntity> searchEventsByDate(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("date") String date);

}

