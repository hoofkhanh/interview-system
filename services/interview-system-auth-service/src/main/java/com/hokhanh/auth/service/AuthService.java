package com.hokhanh.auth.service;

import org.springframework.stereotype.Service;

import com.hokhanh.auth.redis.AuthRedisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthRedisService authRedisService;

	public boolean isTokenBlocked(String accessToken) {
		return authRedisService.isTokenInBlacklist(accessToken);
	}
}
