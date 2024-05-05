package pi.com.calendarservice.web;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
//@CrossOrigin(origins = "*")
public class TestRestController {
    @GetMapping("/hello")
    public String hello(@RequestParam("prompt") String prompt, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        System.out.println("Prompt: " + prompt);
        System.out.println("Token: " + token);
        System.out.println("Rest controller called");
        return "Hello from Gateway!";
    }
}

