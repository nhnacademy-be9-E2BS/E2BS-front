package com.nhnacademy.front.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nhnacademy.front.common.interceptor.FeignCookieInterceptor;

import feign.RequestInterceptor;

@Configuration
public class FeignConfig {

	@Bean
	public RequestInterceptor feignCookieInterceptor() {
		return new FeignCookieInterceptor();
	}

}
