package com.example.promp_gpt.service.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("calendar-service")
public interface CalenderClient {
    //@RequestMapping(method = RequestMethod.POST, value = "myCards", consumes = "application/json")
  //  @PostMapping("/events")
    //List<EventEntity> getEvent( String Token, @RequestBody EventEntity event);
}

