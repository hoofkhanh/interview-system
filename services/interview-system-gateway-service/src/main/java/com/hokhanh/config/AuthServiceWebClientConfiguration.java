package com.hokhanh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AuthServiceWebClientConfiguration {

	@Bean
	WebClient authServiceWebClient(WebClient.Builder builder, @Value("${auth-service.base-url}") String baseUrl) {
		return builder.baseUrl(baseUrl).build();
	}
}
