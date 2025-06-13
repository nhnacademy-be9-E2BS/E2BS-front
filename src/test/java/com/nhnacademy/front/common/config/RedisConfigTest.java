package com.nhnacademy.front.common.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

class RedisConfigUnitTest {

	RedisConfig redisConfig = new RedisConfig();

	@Test
	void testRedisObjectMapper() {
		// when
		ObjectMapper objectMapper = redisConfig.redisObjectMapper();

		// then
		assertNotNull(objectMapper);
	}

	@Test
	void testRedisTemplate() {
		// given
		RedisConnectionFactory mockFactory = mock(RedisConnectionFactory.class);

		// when
		RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate(mockFactory);

		// then
		assertNotNull(redisTemplate);
		assertEquals(mockFactory, redisTemplate.getConnectionFactory());
		assertInstanceOf(StringRedisSerializer.class, redisTemplate.getKeySerializer());
		assertInstanceOf(GenericJackson2JsonRedisSerializer.class, redisTemplate.getValueSerializer());
	}

}