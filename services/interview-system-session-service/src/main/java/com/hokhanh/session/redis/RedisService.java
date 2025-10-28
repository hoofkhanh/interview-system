package com.hokhanh.session.redis;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;


@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;
	
	public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public <T> T hget(String key, String field, Class<T> clazz) {
        Object value = redisTemplate.opsForHash().get(key, field);
        if (value == null) return null;
        return objectMapper.convertValue(value, clazz);
    }

    public void hdel(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }
}
