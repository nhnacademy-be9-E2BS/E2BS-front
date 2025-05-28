package com.nhnacademy.front.common.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuthenticationPaycoSuccessHandler implements AuthenticationSuccessHandler {
	private static final String ROOT_URL = "/";
	private static final String LOGIN_URL = "/login";
	private static final String ADMIN_LOGIN_URL = "/admin/login";

	private final AuthService authService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		String url = request.getHeader("Referer");
		String memberId = oAuth2User.getAttribute("idNo");

		boolean isAdmin = authentication.getAuthorities().stream()
			.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

		boolean isMember = authentication.getAuthorities().stream()
			.anyMatch(authority -> authority.getAuthority().equals("ROLE_MEMBER"));

		if (isAdmin && !url.contains(ADMIN_LOGIN_URL)) {
			SecurityContextHolder.clearContext(); // Security 초기화
			response.sendRedirect(LOGIN_URL);
		} else if (isMember && url.contains(ADMIN_LOGIN_URL)) {
			SecurityContextHolder.clearContext(); // Security 초기화
			response.sendRedirect(ADMIN_LOGIN_URL);
		} else {
			RequestJwtTokenDTO requestJwtTokenDTO = new RequestJwtTokenDTO(memberId);
			authService.postAuthCreateJwtToken(requestJwtTokenDTO, response, request);

			response.sendRedirect(ROOT_URL);
		}

	}
}
