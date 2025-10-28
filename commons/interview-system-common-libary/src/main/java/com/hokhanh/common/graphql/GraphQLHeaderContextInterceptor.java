package com.hokhanh.common.graphql;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;

import com.hokhanh.common.httpHeader.HttpHeadersConstants;

import reactor.core.publisher.Mono;

public class GraphQLHeaderContextInterceptor implements WebGraphQlInterceptor {
	
	@Override
	public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
		String authId = request.getHeaders().getFirst(HttpHeadersConstants.HEADER_AUTH_ID);
	    String userId = request.getHeaders().getFirst(HttpHeadersConstants.HEADER_USER_ID);
	    String roleName = request.getHeaders().getFirst(HttpHeadersConstants.HEADER_ROLE_NAME);
	    
		request.configureExecutionInput((executionInput, builder) -> {
			Map<String, Object> contextMap = new HashMap<>();
			if (userId != null && roleName != null && authId != null) {
				contextMap.put(HttpHeadersConstants.HEADER_AUTH_ID, UUID.fromString(authId));
				contextMap.put(HttpHeadersConstants.HEADER_USER_ID, UUID.fromString(userId));
				contextMap.put(HttpHeadersConstants.HEADER_ROLE_NAME, roleName);
			}
			return builder.graphQLContext(contextMap).build();
		});
		
		return chain.next(request);
	}
}