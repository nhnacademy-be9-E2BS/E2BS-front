package com.nhnacademy.front.common.interceptor;

import java.util.List;
import java.util.Objects;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CartInterceptor implements HandlerInterceptor {

	private List<String> excludeUrls = List.of("/members/order", "/customers/order", "/order/payment", "/order/point", "/order/success", "/order/auth", "/order/confirm");
	private static final String TARGET_COOKIE_NAME = "orderCart";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		String requestURI = request.getRequestURI();

		if (!excludeUrls.contains(requestURI)) {
			Cookie[] cookies = request.getCookies();
			if (Objects.nonNull(cookies)) {
				for (Cookie cookie : cookies) {
					if (TARGET_COOKIE_NAME.equals(cookie.getName())) {
						cookie.setValue("");
						cookie.setPath("/");
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					}
				}
			}
		}

		return true;
	}

}
