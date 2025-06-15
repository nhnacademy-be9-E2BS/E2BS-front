package com.nhnacademy.front.common.util;

import java.util.Objects;

import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

	/**
	 * 	이 클래스는 static 으로 공통 사용하기 때문에
	 * 	private 생성자를 선언하여 다른 곳에서 인스턴스 생성을 못하도록 막는다.
	 */
	private CookieUtil() {
	}

	private static final int EXPIRY = 60 * 60 * 24; // 1일

	public static void setCookie(String cookieName, HttpServletResponse response, String value) {
		ResponseCookie responseCookie =  ResponseCookie.from(cookieName, value)
			.path("/")
			.sameSite("Lax")
			.secure(true)
			.httpOnly(true)
			.maxAge(EXPIRY)
			.build();

		response.addHeader("Set-Cookie", responseCookie.toString());
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
		ResponseCookie responseCookie = ResponseCookie.from(cookieName, "")
			.path("/")
			.maxAge(0)
			.httpOnly(true)
			.secure(true)
			.sameSite("Lax")
			.build();

		response.addHeader("Set-Cookie", responseCookie.toString());
	}

}
