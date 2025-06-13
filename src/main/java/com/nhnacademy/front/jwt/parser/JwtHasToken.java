package com.nhnacademy.front.jwt.parser;

import com.nhnacademy.front.jwt.rule.JwtRule;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class JwtHasToken {
	private JwtHasToken() {
		throw new UnsupportedOperationException("기본 생성자를 생성할 수 없습니다.");
	}

	public static boolean hasToken(HttpServletRequest request) {
		return extractAccessTokenFromCookie(request) != null;
	}

	private static String extractAccessTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(JwtRule.JWT_ISSUE_HEADER.getValue())) {
				return cookie.getValue();
			}
		}

		return null;
	}

}
