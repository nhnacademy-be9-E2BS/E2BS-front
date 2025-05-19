package com.nhnacademy.front.jwt.parser;

import org.springframework.stereotype.Component;

import com.nhnacademy.front.jwt.rule.JwtRule;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtGetMemberId {

	public static String jwtGetMemberId(HttpServletRequest request) {
		String accessToken = "";

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(JwtRule.JWT_ISSUE_HEADER.getValue())) {
				accessToken = cookie.getValue();
			}
		}

		return JwtMemberIdParser.getMemberId(accessToken);
	}

}
