package com.hokhanh.auth.redis;

import java.time.Duration;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenCacheService {
	private final RedisService redisService;
	
	private static final String ACCESS_TOKEN_BLACKLIST_REDIS_KEY = "access_token:blacklist:";
	private static final String REFRESH_TOKEN_REDIS_KEY = "refresh_token:";
	
	public boolean isTokenInBlacklist(String accessToken) {
		String token = redisService.get(ACCESS_TOKEN_BLACKLIST_REDIS_KEY + accessToken, String.class);
		return token != null ? true : false;
	}
	
	public void cacheRefreshToken(String refreshToken, Duration ttl) {
		redisService.set(REFRESH_TOKEN_REDIS_KEY + refreshToken, refreshToken, ttl);
	}
	
	public void deleteRefreshToken(String refreshToken) {
		redisService.delete(REFRESH_TOKEN_REDIS_KEY + refreshToken);
	}
	
	public void cacheAccessTokenToBlacklist(String accessToken, Duration ttl) {
		redisService.set(ACCESS_TOKEN_BLACKLIST_REDIS_KEY + accessToken, accessToken, ttl);
	}
	
	public String getCachedRefreshToken(String rtFromCookie) {
		return redisService.get(REFRESH_TOKEN_REDIS_KEY + rtFromCookie, String.class);
	}
}
