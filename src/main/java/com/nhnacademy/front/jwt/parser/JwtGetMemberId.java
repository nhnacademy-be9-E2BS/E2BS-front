package com.nhnacademy.front.jwt.parser;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.nhnacademy.front.account.member.exception.NotFoundMemberIdException;
import com.nhnacademy.front.jwt.rule.JwtRule;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtGetMemberId {

	public static String jwtGetMemberId(HttpServletRequest request) {
		String accessToken = "";

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(JwtRule.JWT_ISSUE_HEADER.getValue())) {
					accessToken = cookie.getValue();
				}
			}
		}

		String memberId = JwtMemberIdParser.getMemberId(accessToken);
		if (Objects.isNull(memberId)) {
			throw new NotFoundMemberIdException("아이디에 해당하는 아이디를 찾지 못했습니다.");
		}

		return JwtMemberIdParser.getMemberId(accessToken);
	}

}
