package com.example.promp_gpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PrompGptApplication {
	public static void main(String[] args) {
		SpringApplication.run(PrompGptApplication.class, args);
	}

}
