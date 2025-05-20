package com.nhnacademy.front.common.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 로그인 시도 실패 시 뜨는 에러 메시지
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {

		String loginErrorMessage = exception.getMessage();

		response.sendRedirect("/login?error=" + URLEncoder.encode(loginErrorMessage, StandardCharsets.UTF_8));

	}

}
