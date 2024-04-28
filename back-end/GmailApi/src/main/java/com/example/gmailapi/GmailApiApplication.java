package com.example.gmailapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GmailApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmailApiApplication.class, args);
	}
	@Bean
	CommandLineRunner runner(EmailService emailService) {
		return args -> {
			System.out.println("Application started...");
			emailService.sendEmail("abderrazzak.nouari@gmail.com","Test Subject","Test Body");
		};
	}

}
