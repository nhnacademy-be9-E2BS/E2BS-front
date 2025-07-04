package com.nhnacademy.front.common.handler;

import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.nhnacademy.front.jwt.parser.JwtMemberIdParser;
import com.nhnacademy.front.jwt.rule.JwtRule;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String accessToken = "";

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(JwtRule.JWT_ISSUE_HEADER.getValue())) {
					accessToken = cookie.getValue();

					cookie.setValue(null);
					cookie.setPath("/");
					cookie.setHttpOnly(true);
					cookie.setSecure(true);
					cookie.setMaxAge(0);

					response.addCookie(cookie);
					break;
				}
			}
		}

		String memberId = JwtMemberIdParser.getMemberId(accessToken);
		if (Objects.nonNull(memberId)) {
			String refreshKey = JwtRule.REFRESH_PREFIX.getValue() + ":" + memberId;
			redisTemplate.delete(refreshKey);
		}

		HttpSession session = request.getSession();
		session.invalidate();
	}

}
