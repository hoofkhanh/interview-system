package com.hokhanh.auth.redis;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthRedisService {
	private final RedisService redisService;
	
	public static final String ACCESS_TOKEN_BLACKLIST_REDIS_KEY = "access_token:blacklist:";
	
	public boolean isTokenInBlacklist(String accessToken) {
		String token = redisService.get(ACCESS_TOKEN_BLACKLIST_REDIS_KEY + accessToken, String.class);
		return token != null ? true : false;
	}
}
