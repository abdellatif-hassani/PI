package com.example.promp_gpt.service.clients;


import com.example.promp_gpt.entities.EventEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

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
    void deleteEvent(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EventEntity event);
}

