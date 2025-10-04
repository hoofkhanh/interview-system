package com.hokhanh.auth.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;

import com.hokhanh.auth.constants.AuthenticationConstants;
import com.hokhanh.auth.utils.CookieUtil;
import com.hokhanh.common.jwt.JwtPropertyConstants;

import graphql.GraphQLContext;
import reactor.core.publisher.Mono;

@Configuration
public class GraphQLContextInterceptor implements WebGraphQlInterceptor {
	
	@Override
	public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
		// here run before go into controller
		HttpCookie refreshTokenFromCookie = request.getCookies()
				.getFirst(AuthenticationConstants.REFRESH_TOKEN_COOKIE_NAME);
		String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		request.configureExecutionInput((executionInput, builder) -> {
			Map<String, Object> contextMap = new HashMap<>();
			if (refreshTokenFromCookie != null) {
				contextMap.put(AuthenticationConstants.REFRESH_TOKEN_COOKIE_NAME, refreshTokenFromCookie.getValue());
			}
			if (authorization != null) {
				contextMap.put(AuthenticationConstants.AUTHORIZATION_CONTEXT_KEY, authorization);
			}
			return builder.graphQLContext(contextMap).build();
		});

		return chain.next(request).doOnNext(response -> {
			// here run after finishing request
			GraphQLContext ctx = response.getExecutionInput().getGraphQLContext();
			Boolean setCookie = ctx.getOrDefault(AuthenticationConstants.SET_COOKIE_CONTEXT_KEY, false);

			if (Boolean.TRUE.equals(setCookie)) {
				String refreshTokenFromContext = ctx.get(AuthenticationConstants.REFRESH_TOKEN_COOKIE_NAME);
				CookieUtil.setHttpOnlyCookie(response, AuthenticationConstants.REFRESH_TOKEN_COOKIE_NAME,
						refreshTokenFromContext, JwtPropertyConstants.REFRESH_TOKEN_EXPIRATION,
						AuthenticationConstants.REFRESH_TOKEN_COOKIE_PATH);
			}

			Boolean removeCookie = ctx.getOrDefault(AuthenticationConstants.REMOVE_COOKIE_CONTEXT_KEY, false);
			if (Boolean.TRUE.equals(removeCookie)) {
				CookieUtil.setHttpOnlyCookie(response, AuthenticationConstants.REFRESH_TOKEN_COOKIE_NAME, null, 0,
						AuthenticationConstants.REFRESH_TOKEN_COOKIE_PATH);
			}

		});
	}
}