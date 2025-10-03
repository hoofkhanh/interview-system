package com.hokhanh.auth.redis;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;


@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;
	
	public void set(String key, Object value, Duration ttl) {
	    redisTemplate.opsForValue().set(key, value, ttl);
	}
	
	public <T> T get(String key, Class<T> clazz) {
	    Object value = redisTemplate.opsForValue().get(key);
	    if (value == null) return null;

	    return objectMapper.convertValue(value, clazz);
	}
	
	public void delete(String key) {
		redisTemplate.delete(key);
	}
}
