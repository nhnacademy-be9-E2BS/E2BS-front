package com.nhnacademy.front.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nhnacademy.front.account.auth.service.AuthService;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.cart.service.CartService;
import com.nhnacademy.front.common.filter.JwtAuthenticationFilter;
import com.nhnacademy.front.common.handler.CustomAuthenticationFailureHandler;
import com.nhnacademy.front.common.handler.CustomAuthenticationSuccessHandler;
import com.nhnacademy.front.common.handler.CustomLogoutHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private static final String ROOT_URL = "/";
	private static final String LOGIN_URL = "/members/login";
	private static final String PROCESS_LOGIN_URL = "/member/login";
	private static final String LOGOUT_URL = "/logout";
	private static final String ADMIN_LOGIN_URL = "/admin/login";
	private static final String REGISTER_URL = "/members/register";
	private static final String ACTUATOR_HEALTH = "/actuator/health";

	private final AuthService authService;
	private final CartService cartService;
	private final MemberService memberService;
	private final RedisTemplate<String, String> redisTemplate;
	private final AuthenticationManager authenticationManager;

	@Order(1)
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler(
			cartService, memberService
		);
		CustomLogoutHandler customLogoutHandler = new CustomLogoutHandler(
			redisTemplate
		);
		CustomAuthenticationFailureHandler customAuthenticationFailureHandler = new CustomAuthenticationFailureHandler();

		http.csrf(AbstractHttpConfigurer::disable);
		http.securityMatcher(LOGIN_URL, PROCESS_LOGIN_URL, REGISTER_URL, LOGOUT_URL);
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
				.loginProcessingUrl(PROCESS_LOGIN_URL)
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
			)
			.addFilterBefore(new JwtAuthenticationFilter(authenticationManager, authService, memberService),
				UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
