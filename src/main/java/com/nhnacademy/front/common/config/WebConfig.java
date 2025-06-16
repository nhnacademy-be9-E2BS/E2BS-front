package com.nhnacademy.front.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final ObjectMapper objectMapper;
	private final CategoryInterceptor categoryInterceptor;
	private final MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;
	private final CartInterceptor cartInterceptor;

	@Value("${custom.oauth2.payco-callback-path}")
	private String paycoCallbackPath;

	private static final String ACTUATOR_PATH = "/actuator/**";
	private static final String CSS_PATH = "/**/*.css";
	private static final String JS_PATH = "/**/*.js";
	private static final String PNG_PATH = "/**/*.png";
	private static final String JPG_PATH = "/**/*.jpg";
	private static final String VENDORS_PATH = "/vendors/**";
	private static final String ERROR_PATH = "/error/**";

	/**
	 * 직렬화를 위해 커스터마이징한 ObjectMapper converter에 추가
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
	}

	/**
	 * 접속 시 요청 인터셉터
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(categoryInterceptor)
			.excludePathPatterns(paycoCallbackPath, ACTUATOR_PATH, CSS_PATH, JS_PATH, PNG_PATH, JPG_PATH, VENDORS_PATH, ERROR_PATH);

		registry.addInterceptor(memberNameAndRoleInterceptor)
			.excludePathPatterns("/login/**", "/register/**", "/admin/login/**", "/payco/login/**")
			.excludePathPatterns(paycoCallbackPath, ACTUATOR_PATH, CSS_PATH, JS_PATH, PNG_PATH, JPG_PATH, VENDORS_PATH, ERROR_PATH);

		registry.addInterceptor(cartInterceptor)
			.excludePathPatterns(
				"/customers/login", "/customers/register",
				"/members/order", "/customers/order",
				"/order/auth", "/order/payment", "/order/point", "/order/success", "/order/confirm",
				"/payco/login/**", "/oauth2/authorization/payco", "/id.payco.com/**",
				"/.well-known/**",
				paycoCallbackPath, ACTUATOR_PATH, CSS_PATH, JS_PATH, PNG_PATH, JPG_PATH, VENDORS_PATH, ERROR_PATH
			);
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
