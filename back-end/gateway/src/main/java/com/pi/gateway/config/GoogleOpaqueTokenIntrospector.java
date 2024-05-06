package com.pi.gateway.config;

import com.pi.gateway.dtos.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class GoogleOpaqueTokenIntrospector implements ReactiveOpaqueTokenIntrospector {

    private final WebClient userInfoClient;
    private final String introspectUri="https://www.googleapis.com/oauth2/v3/tokeninfo";
    private final String introspectionUri="http://localhost:9090/oauth2/introspec";
    @Value( "${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value( "${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    public GoogleOpaqueTokenIntrospector(WebClient userInfoClient) {
        this.userInfoClient = userInfoClient;
    }

    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
        URI uri = UriComponentsBuilder.fromUriString(introspectUri)
                .path("")
                .queryParam("access_token", token)
                .build()
                .toUri();
        return userInfoClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .map(userInfo -> {
                    Map<String, Object> attributes = new HashMap<>();
                        attributes.put("sub", userInfo.sub());
                        attributes.put("name", userInfo.name());
                        attributes.put("token",token);
                    return new OAuth2IntrospectionAuthenticatedPrincipal(userInfo.name(), attributes, Collections.emptyList());
                });
    }

}
