package com.nhnacademy.front.common.util;

import java.util.Objects;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GuestCookieUtil {

	public static final String COOKIE_NAME = "guestKey";
	private static final int EXPIRY = 60 * 60 * 24; // 1일

	public static void setGuestCookie(HttpServletResponse response, String value) {
		Cookie cookie = new Cookie(COOKIE_NAME, value);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(EXPIRY);
		response.addCookie(cookie);
	}

	public static String getGuestKey(HttpServletRequest request) {
		if (Objects.nonNull(request.getCookies())) {
			for (Cookie cookie : request.getCookies()) {
				if (COOKIE_NAME.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public static void clearGuestCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie(COOKIE_NAME, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
