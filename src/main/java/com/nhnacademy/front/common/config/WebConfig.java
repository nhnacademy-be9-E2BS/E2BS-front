package com.nhnacademy.front.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final ObjectMapper objectMapper;
	private final CategoryInterceptor categoryInterceptor;
	private final MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	/**
	 * 직렬화를 위해 커스터마이징한 ObjectMapper converter에 추가
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
	}

	/**
	 * 접속 시 카테고리 조회 요청 인터셉터
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(categoryInterceptor)
			.excludePathPatterns("/actuator/**")
			.excludePathPatterns("/**/*.css")
			.excludePathPatterns("/**/*.js")
			.excludePathPatterns("/**/*.png")
			.excludePathPatterns("/**/*.jpg")
			.excludePathPatterns("/vendors/**")
			.excludePathPatterns("/error/**")
			.excludePathPatterns("/login/oauth2/code/payco");

		registry.addInterceptor(memberNameAndRoleInterceptor)
			.excludePathPatterns("/login/**")
			.excludePathPatterns("/register/**")
			.excludePathPatterns("/admin/login/**")
			.excludePathPatterns("/payco/login/**")

			.excludePathPatterns("/login/oauth2/code/payco")
			.excludePathPatterns("/actuator/**")
			.excludePathPatterns("/**/*.css")
			.excludePathPatterns("/**/*.js")
			.excludePathPatterns("/**/*.png")
			.excludePathPatterns("/**/*.jpg")
			.excludePathPatterns("/vendors/**")
			.excludePathPatterns("/error/**");

	}
}
