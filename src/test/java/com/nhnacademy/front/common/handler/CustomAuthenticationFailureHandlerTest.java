package com.nhnacademy.front.common.handler;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class CustomAuthenticationFailureHandlerTest {

	private CustomAuthenticationFailureHandler failureHandler;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private final AuthenticationException exception = mock(AuthenticationException.class);

	@BeforeEach
	void setUp() {
		failureHandler = new CustomAuthenticationFailureHandler();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		when(exception.getMessage()).thenReturn("Invalid credentials");
	}

	@Test
	@DisplayName("관리자 로그인 실패시 리다이렉트 테스트")
	void adminLoginFailureRedirect() throws IOException, ServletException {
		// given
		when(request.getHeader("Referer")).thenReturn("http://localhost/admin/login");

		// when
		failureHandler.onAuthenticationFailure(request, response, exception);

		// then
		verify(response).sendRedirect(contains("/admin/login?error="));
	}

	@SuppressWarnings("squid:S2699")
	@Test
	@DisplayName("비회원 로그인 실패시 리다이렉트 테스트")
	void customerLoginFailureRedirect() throws IOException, ServletException {
		// given
		when(request.getHeader("Referer")).thenReturn("http://localhost/customer/login");

		// when
		failureHandler.onAuthenticationFailure(request, response, exception);

		// then
		verify(response).sendRedirect(contains("/customer/login?error="));
	}

	@Test
	@DisplayName("회원 로그인 실패시 리다이렉트 테스트")
	void defaultLoginFailureRedirect() throws IOException, ServletException {
		// given
		when(request.getHeader("Referer")).thenReturn("http://localhost/somewhere/else");

		// when
		failureHandler.onAuthenticationFailure(request, response, exception);

		// then
		verify(response).sendRedirect(contains("/members/login?error="));
	}

}
