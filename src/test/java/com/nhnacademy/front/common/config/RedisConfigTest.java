package com.nhnacademy.front.common.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

class RedisConfigTest {

	RedisConfig redisConfig = new RedisConfig();

	@Test
	@DisplayName("ObjectMapper 생성 테스트")
	void testRedisObjectMapper() {
		// when
		ObjectMapper objectMapper = redisConfig.redisObjectMapper();

		// then
		assertNotNull(objectMapper);
	}

	@Test
	@DisplayName("RedisTemplate 생성 테스트")
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