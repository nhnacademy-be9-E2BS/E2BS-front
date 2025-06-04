package com.nhnacademy.front.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {

	// @Value("${spring.data.redis.host}")
	// private String host;
	//
	// @Value("${spring.data.redis.port}")
	// private int port;
	//
	// @Value("${spring.data.redis.password}")
	// private String password;
	//
	// @Value("${spring.data.redis.database}")
	// private int database;
	//
	// @Bean
	// public RedisConnectionFactory redisConnectionFactory() {
	// 	RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
	// 	redisStandaloneConfiguration.setHostName(host);
	// 	redisStandaloneConfiguration.setPort(port);
	// 	redisStandaloneConfiguration.setPassword(password);
	// 	redisStandaloneConfiguration.setDatabase(database);
	// 	return new LettuceConnectionFactory(redisStandaloneConfiguration);
	// }

	@Bean
	public ObjectMapper redisObjectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> sessionRedisTemplate = new RedisTemplate<>();
		sessionRedisTemplate.setConnectionFactory(redisConnectionFactory);
		sessionRedisTemplate.setKeySerializer(new StringRedisSerializer());
		/**
		 * 넣을 때 @class 부분을 제거한 후 Json으로 변환하기 위해 선언
		 * 객체를 판별하기 위해 ObjectMapper를 인자로 전달해줌
		 */
		sessionRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper()));
		sessionRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
		sessionRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper()));

		return sessionRedisTemplate;
	}

}
