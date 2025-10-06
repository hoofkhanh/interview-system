package com.hokhanh.auth.utils;

import java.time.Duration;

import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.ResponseCookie;

import jakarta.ws.rs.core.HttpHeaders;

public class CookieUtil {
	
	public static void setHttpOnlyCookie(WebGraphQlResponse response, String name, String value, Duration ttl, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(true)         
            .path(path)
            .maxAge(ttl)
            .sameSite("None")   
            .build();

        response.getResponseHeaders().add(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}