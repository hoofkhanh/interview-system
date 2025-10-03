package com.hokhanh.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurity {

	@Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,GraphQLOperationFilter authenticationFilter, 
    		JwtAuthorizationFilter jwtFilter
    		) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                    .pathMatchers("/*/graphql").permitAll()
                    .anyExchange().authenticated()
                )
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
