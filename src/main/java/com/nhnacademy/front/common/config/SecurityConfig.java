package com.nhnacademy.front.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nhnacademy.front.account.auth.service.AuthService;
import com.nhnacademy.front.cart.service.CartService;
import com.nhnacademy.front.common.handler.CustomAuthenticationFailureHandler;
import com.nhnacademy.front.common.handler.CustomAuthenticationSuccessHandler;
import com.nhnacademy.front.common.handler.CustomLogoutHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private static final String ROOT_URL = "/";
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_URL = "/logout";
	private static final String ADMIN_LOGIN_URL = "/admin/login";
	private static final String REGISTER_URL = "/register";
	private static final String ACTUATOR_HEALTH = "/actuator/health";

	private final AuthService authService;
	private final CartService cartService;
	private final RedisTemplate<String, String> redisTemplate;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler(
			authService, cartService
		);
		CustomLogoutHandler customLogoutHandler = new CustomLogoutHandler(
			redisTemplate
		);
		CustomAuthenticationFailureHandler customAuthenticationFailureHandler = new CustomAuthenticationFailureHandler();

		/**
		 *  권한있는 회원만 접근 가능한 URL 설정
		 */
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(LOGIN_URL, ROOT_URL, REGISTER_URL, ADMIN_LOGIN_URL, ACTUATOR_HEALTH).permitAll()
				.requestMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/scss/**", "/vendors/**",
					"/Aroma Shop-doc/**").permitAll()
				.anyRequest().permitAll()
			)

			/**
			 * 로그인 기능
			 */
			.formLogin(form -> form
				.loginPage(LOGIN_URL)
				.loginProcessingUrl(LOGIN_URL)
				.usernameParameter("memberId")
				.passwordParameter("customerPassword")
				.successHandler(customAuthenticationSuccessHandler)
				.failureHandler(customAuthenticationFailureHandler)
				.permitAll()
			)
			/**
			 * 로그아웃 기능
			 */
			.logout(logout -> logout
				.addLogoutHandler(customLogoutHandler)
				.logoutUrl(LOGOUT_URL)
			);

		return http.build();
	}

	/**
	 * password 암호화를 위한 빈 객체 추가
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
