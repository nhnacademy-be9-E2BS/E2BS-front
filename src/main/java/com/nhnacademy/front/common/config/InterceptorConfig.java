package com.nhnacademy.front.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.home.service.HomeService;
import com.nhnacademy.front.product.category.service.UserCategoryService;

@Configuration
public class InterceptorConfig {
	@Bean
	public CategoryInterceptor categoryInterceptor(@Lazy UserCategoryService userCategoryService,
		RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
		return new CategoryInterceptor(userCategoryService, redisTemplate, objectMapper);
	}

	@Bean
	public MemberNameAndRoleInterceptor memberNameAndRoleInterceptor(@Lazy HomeService homeService) {
		return new MemberNameAndRoleInterceptor(homeService);
	}
}
