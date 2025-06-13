package com.nhnacademy.front.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nhnacademy.front.account.auth.service.AuthService;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.cart.service.CartService;
import com.nhnacademy.front.common.filter.JwtAuthenticationAdminFilter;
import com.nhnacademy.front.common.handler.CustomAuthenticationFailureHandler;
import com.nhnacademy.front.common.handler.CustomAuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityAdminConfig {
	private static final String ADMIN_LOGIN = "/admin/login";
	private static final String ADMIN_PROCESS_LOGIN = "/admin/admin/login";

	private final AuthenticationManager authenticationManager;
	private final AuthService authService;
	private final MemberService memberService;
	private final CartService cartService;

	@Order(2)
	@Bean
	public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
		CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler(
			cartService, memberService
		);
		CustomAuthenticationFailureHandler customAuthenticationFailureHandler = new CustomAuthenticationFailureHandler();

		http
			.securityMatcher(ADMIN_LOGIN, ADMIN_PROCESS_LOGIN)

			.csrf(AbstractHttpConfigurer::disable)
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
				.loginPage(ADMIN_LOGIN)
				.loginProcessingUrl(ADMIN_PROCESS_LOGIN)
				.usernameParameter("memberId")
				.passwordParameter("customerPassword")
				.successHandler(customAuthenticationSuccessHandler)
				.failureHandler(customAuthenticationFailureHandler)
			)

			.addFilterBefore(new JwtAuthenticationAdminFilter(authenticationManager, authService, memberService),
				UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
