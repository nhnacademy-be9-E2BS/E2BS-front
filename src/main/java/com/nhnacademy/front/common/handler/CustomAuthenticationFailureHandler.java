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
	private static final String ADMIN_LOGIN_URL = "/admin/login";

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		String url = request.getHeader("Referer");

		String loginErrorMessage = exception.getMessage();

		if (url.contains(ADMIN_LOGIN_URL)) {
			response.sendRedirect("/admin/login?error=" + URLEncoder.encode(loginErrorMessage, StandardCharsets.UTF_8));
		} else {
			response.sendRedirect("/login?error=" + URLEncoder.encode(loginErrorMessage, StandardCharsets.UTF_8));
		}
	}

}
