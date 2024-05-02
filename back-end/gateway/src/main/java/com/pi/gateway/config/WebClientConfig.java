package com.pi.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientConfig {
    @Bean
    public WebClient userInfoClient() {
        return WebClient.builder().build();
    }
}
