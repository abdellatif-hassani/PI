package com.example.gmailapi;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GmailApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmailApiApplication.class, args);
	}


}
