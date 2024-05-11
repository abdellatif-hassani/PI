package com.pi.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {



	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("prompt_gpt", r -> r.path("/prompt_service/**")
						.filters(f -> f.rewritePath("/prompt_service(?<segment>.*)","/${segment}")
								.filter((exchange, chain) -> {
									return ReactiveSecurityContextHolder.getContext()
											.map(securityContext -> (OAuth2AccessToken) securityContext.getAuthentication().getCredentials())
											.map(OAuth2AccessToken::getTokenValue)
											.map(token -> {
												exchange.getRequest().mutate().header("Authorization", "Bearer "+token);
												return exchange;
											})
											.defaultIfEmpty(exchange)
											.flatMap(chain::filter);
								}))
						.uri("lb://prompt"))
				.build();
	}


}
