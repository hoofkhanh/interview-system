package com.hokhanh.security;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;

import org.springframework.http.HttpHeaders;

import graphql.language.Document;
import graphql.language.Definition;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.language.Selection;
import graphql.parser.InvalidSyntaxException;
import graphql.parser.Parser;


@Component
public class GraphQLOperationFilter implements WebFilter{

	private final ObjectMapper objectMapper = new ObjectMapper();

	private static final List<String> PUBLIC_OPERATIONS = List.of(
			"signupInterviewer"
	);
	
	private static final List<String> INTERNAL_OPERATIONS = List.of(
			"isTokenBlockedInternal", "createUserInternal", "userByEmailInternal"
		);
	
	@Value("${spring.graphql.http.path}")
	private String graphqlPath;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		
		String path = request.getURI().getPath();
		if (path.endsWith(graphqlPath) && (request.getMethod() == HttpMethod.POST || request.getMethod() == HttpMethod.GET)) {
			return DataBufferUtils.join(request.getBody()).flatMap(dataBuffer -> {
	            byte[] bytes = new byte[dataBuffer.readableByteCount()];
	            dataBuffer.read(bytes);
	            DataBufferUtils.release(dataBuffer);

	            String body = new String(bytes, StandardCharsets.UTF_8);
	            
	            String graphqlQuery = extractGraphqlQueryFromJson(body);

	            Pair<String, String> operation = extractOperationTypeAndName(graphqlQuery);
	            if(operation == null) {
	            	return JwtAuthUtils.onError(exchange, "Operation is null", HttpStatus.BAD_REQUEST);
	            }
	            
	            String operationName = operation.getRight();
	            if(operationName != null 
	            		&& INTERNAL_OPERATIONS
	            		.stream().anyMatch(blockedOperation -> blockedOperation.equalsIgnoreCase(operationName))) {
	            	return JwtAuthUtils.onError(exchange, "Can't access this api", HttpStatus.UNAUTHORIZED);
	    		}
	            
	            if ( operationName != null 
	            	    && PUBLIC_OPERATIONS.stream()
	                    .anyMatch(required -> required.equalsIgnoreCase(operationName))) {
	                return forwardRequestWithBody(exchange, chain, bytes, false, operationName);
	            } 
	            
	            return forwardRequestWithBody(exchange, chain, bytes, true, operationName);
	        });
		}
		
		return JwtAuthUtils.onError(exchange, "Use http post or get", HttpStatus.UNAUTHORIZED);
	}
	
	
	private String extractGraphqlQueryFromJson(String bodyJson) {
        try {
            JsonNode jsonNode = objectMapper.readTree(bodyJson);
            if (jsonNode.has("query")) {
                return jsonNode.get("query").asText();
            } else {
                throw new IllegalArgumentException("Missing 'query' field in JSON body");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON input: " + e.getMessage(), e);
        }
    }

	private static Pair<String, String> extractOperationTypeAndName(String body) {
	    if (body == null || body.trim().isEmpty()) {
	        System.out.println("Empty GraphQL body");
	        return null;
	    }

	    try {
	        Document document = Parser.parse(body);

	        for (Definition<?> definition : document.getDefinitions()) {
	            if (definition instanceof OperationDefinition opDef) {
	                String operationType = opDef.getOperation().name(); // query/mutation/subscription
	                String operationName = opDef.getName(); // optional

	                // Nếu không có tên, lấy tên field đầu tiên (vd: logout)
	                if (operationName == null 
	                    && opDef.getSelectionSet() != null 
	                    && !opDef.getSelectionSet().getSelections().isEmpty()) {

	                    Selection<?> selection = opDef.getSelectionSet().getSelections().get(0);
	                    if (selection instanceof Field field) {
	                        operationName = field.getName();
	                    }
	                }

	                return Pair.of(operationType.toLowerCase(), operationName);
	            }
	        }

	    } catch (InvalidSyntaxException e) {
	        System.out.println("Invalid GraphQL syntax: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("Error extracting operation info: " + e.getMessage());
	    }

	    return null;
	}


	private Mono<Void> forwardRequestWithBody(ServerWebExchange exchange, WebFilterChain chain, byte[] bodyBytes
			, boolean isRequireJwt, String operationName) {
	    Flux<DataBuffer> bodyFlux = Flux.defer(() -> {
	        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bodyBytes);
	        return Mono.just(buffer);
	    });

	    ServerHttpRequest originalRequest = exchange.getRequest();

	    ServerHttpRequest decoratedRequest = new ServerHttpRequestDecorator(originalRequest) {
	        @Override
	        public Flux<DataBuffer> getBody() {
	            return bodyFlux;
	        }

	        @Override
	        public HttpHeaders getHeaders() {
	            HttpHeaders headers = new HttpHeaders();
	            headers.putAll(super.getHeaders());

	            headers.remove(HttpHeaders.CONTENT_LENGTH);
	            headers.setContentLength(bodyBytes.length);
	            headers.set(JwtAuthUtils.HEADER_OPERATION_NAME, operationName);
	            headers.set(JwtAuthUtils.HEADER_REQUIRE_JWT, Boolean.toString(isRequireJwt));

	            return headers;
	        }
	    };

	    return chain.filter(exchange.mutate().request(decoratedRequest).build());
	}

}
