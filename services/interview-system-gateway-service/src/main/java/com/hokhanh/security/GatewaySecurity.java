package com.hokhanh.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurity {

	@Bean
	SecurityWebFilterChain securityWebFilterChain(
	        ServerHttpSecurity http,
	        GraphQLOperationFilter authenticationFilter, 
	        JwtAuthorizationFilter jwtFilter) {

	    return http
	    		.csrf(csrf -> csrf.disable())
	            .authorizeExchange(exchanges -> exchanges
	                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	                .pathMatchers("/*/graphql").permitAll()
	                .anyExchange().authenticated()
	            )
	            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
	            .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
	            .build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
	    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(Arrays.asList("*"));
	    configuration.setAllowCredentials(true);
	    
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}

}
