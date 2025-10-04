package com.hokhanh.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.hokhanh.client.AuthClient;
import com.hokhanh.common.httpHeader.HttpHeadersConstants;
import com.hokhanh.jwt.JwtService;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthorizationFilter implements WebFilter{
	public static final String INTERVIEWER_ROLE = "INTERVIEWER";
	public static final String CANDIDATE_ROLE = "CANDIDATE";

	private static final List<String> INTERVIEWER_REQUIRED_OPERATIONS = List.of();
	private static final List<String> CANDIDATE_REQUIRED_OPERATIONS = 
		List.of
		(
		);
	
	@Autowired
	private AuthClient authClient;

	private JwtService jwtService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		String requireJwt = request.getHeaders().getFirst(JwtAuthUtils.HEADER_REQUIRE_JWT);
		if ("false".equalsIgnoreCase(requireJwt)) {
			return chain.filter(exchange);
		}

		// check
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return JwtAuthUtils.onError(exchange, "Do not empty bearer", HttpStatus.UNAUTHORIZED);
		}

		String token = authHeader.substring(7).trim();
		if (StringUtils.isEmpty(token)) {
			return JwtAuthUtils.onError(exchange, "Token is empty", HttpStatus.UNAUTHORIZED);
		}
		
		try {
			if (!jwtService.isTokenValid(token) ) {
				return JwtAuthUtils.onError(exchange, "Token is invalid", HttpStatus.UNAUTHORIZED);
			}
			
			return authClient.isTokenBlockedInternal(token)
				.flatMap(isBlocked -> {
					if(isBlocked) {
						return JwtAuthUtils.onError(exchange, "Token is blocked", HttpStatus.UNAUTHORIZED);
					}
					
					return processValidateToken(exchange, chain, token);
				});
		} catch (JwtException e) {
			return JwtAuthUtils.onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return JwtAuthUtils.onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	private Mono<Void> processValidateToken(ServerWebExchange exchange, WebFilterChain chain, String token){
		Claims claims = jwtService.extractAllClaims(token);

		String tokenType = claims.get("type", String.class);
		if (!"access".equals(tokenType)) {
			return JwtAuthUtils.onError(exchange, "Token type must be access", HttpStatus.UNAUTHORIZED);
		}

		String authId = claims.getSubject();
		String userId = claims.get("userId", String.class);
		String roleName = claims.get("roleName", String.class);
		
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(authId) || StringUtils.isEmpty(roleName)) {
			return JwtAuthUtils.onError(exchange, "The claims data is empty", HttpStatus.UNAUTHORIZED);
		}
		
		return checkRole(exchange, exchange.getRequest(), roleName)
				.switchIfEmpty(Mono.defer(() -> {
					ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
							.header(HttpHeadersConstants.HEADER_AUTH_ID, authId)
							.header(HttpHeadersConstants.HEADER_USER_ID, userId)
							.header(HttpHeadersConstants.HEADER_ROLE_NAME, roleName)
							.build();

					ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
					return chain.filter(mutatedExchange);
				}));
	}
	
	private Mono<Void> checkRole(ServerWebExchange exchange, ServerHttpRequest request, String roleName){
		String operationName = request.getHeaders().getFirst(JwtAuthUtils.HEADER_OPERATION_NAME);
		if(CANDIDATE_REQUIRED_OPERATIONS
				.stream().anyMatch(x -> x.equalsIgnoreCase(operationName))
				&& !CANDIDATE_ROLE.equalsIgnoreCase(roleName)) {
			return JwtAuthUtils.onError(exchange, "THIS REQUEST IS CANDIDATE ROLE", HttpStatus.FORBIDDEN);
		}
		
		if(INTERVIEWER_REQUIRED_OPERATIONS
				.stream().anyMatch(x -> x.equalsIgnoreCase(operationName))
				&& !INTERVIEWER_ROLE.equalsIgnoreCase(roleName)) {
			return JwtAuthUtils.onError(exchange, "THIS REQUEST IS INTERVIEWER ROLE", HttpStatus.FORBIDDEN);
		}
		
		return Mono.empty();
	}
}
