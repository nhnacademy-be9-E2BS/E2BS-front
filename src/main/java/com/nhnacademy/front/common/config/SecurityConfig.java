package com.nhnacademy.front.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nhnacademy.front.account.auth.service.AuthService;
import com.nhnacademy.front.common.handler.CustomAuthenticationSuccessHandler;
import com.nhnacademy.front.common.handler.CustomLogoutHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthService authService;
	private final RedisTemplate<String, String> redisTemplate;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler(
			authService
		);
		CustomLogoutHandler customLogoutHandler = new CustomLogoutHandler(
			redisTemplate
		);

		/**
		 *  CSRF 보호 기능을 비활성화
		 */
		http.csrf(AbstractHttpConfigurer::disable)
			/**
			 *  권한있는 회원만 접근 가능한 URL 설정
			 */
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll()
			)

			/**
			 * 로그인 기능
			 */
			.formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.usernameParameter("memberId")
				.passwordParameter("customerPassword")
				.successHandler(customAuthenticationSuccessHandler)
				.permitAll()
			)
			/**
			 * 로그아웃 기능
			 */
			.logout(logout -> logout
				.addLogoutHandler(customLogoutHandler)
				.logoutUrl("/logout")
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
