package com.nhnacademy.front.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

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
				.defaultSuccessUrl("/index")
				.permitAll()
			);

		return http.build();
	}

}
