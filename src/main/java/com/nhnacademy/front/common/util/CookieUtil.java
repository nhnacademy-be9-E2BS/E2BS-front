package com.nhnacademy.front.common.util;

import java.util.Objects;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

	private static final int EXPIRY = 60 * 60 * 24; // 1일

	public static void setCookie(String cookieName, HttpServletResponse response, String value) {
		Cookie cookie = new Cookie(cookieName, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(EXPIRY);
		response.addCookie(cookie);
	}

	public static String getCookieValue(String cookieName, HttpServletRequest request) {
		if (Objects.nonNull(request.getCookies())) {
			for (Cookie cookie : request.getCookies()) {
				if (cookieName.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public static void clearCookie(String cookieName, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

}
