package com.nhnacademy.front.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	/**
	 * password 암호화를 위한 빈 객체 추가
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * JwtAuthenticationFilter 에서 사용할 AuthenticationManager
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
		throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
