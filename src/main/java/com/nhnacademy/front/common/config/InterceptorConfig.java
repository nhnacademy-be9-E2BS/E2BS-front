package com.nhnacademy.front.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.FeignCookieInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.home.service.HomeService;
import com.nhnacademy.front.product.category.service.UserCategoryService;

import feign.RequestInterceptor;

@Configuration
public class InterceptorConfig {

	@Bean
	public RequestInterceptor feignCookieInterceptor() {
		return new FeignCookieInterceptor();
	}

	@Bean
	public CategoryInterceptor categoryInterceptor(@Lazy UserCategoryService userCategoryService,
		RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
		return new CategoryInterceptor(userCategoryService, redisTemplate, objectMapper);
	}

	@Bean
	public MemberNameAndRoleInterceptor memberNameAndRoleInterceptor(@Lazy HomeService homeService) {
		return new MemberNameAndRoleInterceptor(homeService);
	}

	@Bean
	public CartInterceptor cartInterceptor() {
		return new CartInterceptor();
	}

}
