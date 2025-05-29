package com.nhnacademy.front.common.interceptor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nhnacademy.front.common.exception.LoginRedirectException;
import com.nhnacademy.front.jwt.parser.JwtHasToken;
import com.nhnacademy.front.jwt.rule.JwtRule;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignCookieInterceptor implements RequestInterceptor {
	private static final String SET_COOKIE = "Set-Cookie";
	private static final String ACCESS_REFRESH = "access-refresh";

	private static final String LOGIN_EXCEPTION_MESSAGE = "로그인을 다시 해주세요.";

	@Override
	public void apply(RequestTemplate requestTemplate) {
		HttpServletRequest request = ((ServletRequestAttributes)Objects
			.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

		if (!JwtHasToken.hasToken(request)) {
			return;
		}

		Optional<Cookie> newCookie = Optional.ofNullable(request.getCookies())
			.map(Arrays::stream)
			.orElse(Stream.empty())
			.filter(v -> v.getName().equals(SET_COOKIE))
			.findFirst();

		StringBuilder cookieHeaders = new StringBuilder();

		if (newCookie.isEmpty()) {
			cookieHeaders.append(extractAccessTokenFromCookie(request));

			requestTemplate.header(JwtRule.JWT_ISSUE_HEADER.getValue(), cookieHeaders.toString());
			return;
		}

		String o = (String)request.getAttribute(ACCESS_REFRESH);
		String data = "";
		if (Objects.nonNull(o)) {
			data = o;
		} else {
			data = newCookie.get().getValue();
		}
		cookieHeaders.append(JwtRule.JWT_ISSUE_HEADER.getValue() + "=" + data);
		requestTemplate.header(JwtRule.JWT_ISSUE_HEADER.getValue(), cookieHeaders.toString());

	}

	private String extractAccessTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			throw new LoginRedirectException(LOGIN_EXCEPTION_MESSAGE);
		}

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(JwtRule.JWT_ISSUE_HEADER.getValue())) {
				return cookie.getName() + "=" + cookie.getValue();
			}
		}

		throw new LoginRedirectException(LOGIN_EXCEPTION_MESSAGE);
	}

}
