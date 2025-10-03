package com.hokhanh.security;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtAuthUtils {

	public static final String HEADER_OPERATION_NAME  = "X-Operation-Name";
	public static final String HEADER_REQUIRE_JWT  = "X-Require-JWT";
	
	public static Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
		ServerHttpResponse response = exchange.getResponse();
	    response.setStatusCode(status);
	    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

	    String json = "{\"message\": \"" + message + "\"}";

	    DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
	    return response.writeWith(Mono.just(buffer));
	}
}
