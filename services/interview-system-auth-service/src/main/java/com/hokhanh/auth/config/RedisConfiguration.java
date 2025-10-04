package com.hokhanh.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RedisConfiguration {

	@Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    
	    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

	    RedisTemplate<String, Object> template = new RedisTemplate<>();
	    template.setConnectionFactory(factory);
	    template.setKeySerializer(new StringRedisSerializer());
	    template.setValueSerializer(serializer);
	    return template;
    }
}
