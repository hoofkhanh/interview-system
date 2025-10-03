package com.hokhanh.auth.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.hokhanh.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	
	@QueryMapping
	public boolean isTokenBlockedInternal(@Argument String accessToken) {
		return authService.isTokenBlocked(accessToken);
	}
}
