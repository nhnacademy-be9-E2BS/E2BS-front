package com.nhnacademy.front.common.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.nhnacademy.front.cart.model.dto.request.RequestCartCountDTO;
import com.nhnacademy.front.cart.service.CartService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * MemberRole 에 따른 로그인 기능 (MEMBER or ADMIN)
 */
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private static final String ROOT_URL = "/";

	private final CartService cartService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		String memberId = authentication.getName();

		HttpSession session = request.getSession();
		session.setAttribute("cartItemsCounts",
			cartService.getCartItemsCounts(new RequestCartCountDTO(memberId, "")));
		session.setAttribute("isMember", true);

		setDefaultTargetUrl(ROOT_URL);
		super.onAuthenticationSuccess(request, response, authentication);

	}

}
