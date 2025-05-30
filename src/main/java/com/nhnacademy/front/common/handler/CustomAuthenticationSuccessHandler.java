package com.nhnacademy.front.common.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.service.AuthService;
import com.nhnacademy.front.cart.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * MemberRole 에 따른 로그인 기능 (MEMBER or ADMIN)
 */
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private static final String ROOT_URL = "/";
	private static final String LOGIN_URL = "/login";
	private static final String ADMIN_LOGIN_URL = "/admin/login";

	private final AuthService authService;
	private final CartService cartService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication) throws IOException {

		String url = request.getHeader("Referer"); // 브라우저의 URL 주소를 가져오기 위한 Referer 사용
		String memberId = authentication.getName();

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

			HttpSession session = request.getSession();
			// session.setAttribute("cartItemsCounts", cartService.getCartItemsCounts(new RequestCartCountDTO(memberId, "")));
			session.setAttribute("isMember", true);

			response.sendRedirect(ROOT_URL);
		}
	}

}
