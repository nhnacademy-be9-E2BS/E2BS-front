package com.nhnacademy.front.common.interceptor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nhnacademy.front.common.exception.LoginRedirectException;
import com.nhnacademy.front.jwt.rule.JwtRule;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignCookieInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

		Optional<Cookie> newCookie = Optional.ofNullable(request.getCookies())
			.map(Arrays::stream)
			.orElse(Stream.empty())
			.filter(v -> v.getName().equals("Set-Cookie"))
			.findFirst();

		StringBuilder cookieHeaders = new StringBuilder();

		if (newCookie.isEmpty()) {
			String path = request.getRequestURI();
			if (path.equals("/") || path.startsWith("/login") ||
				path.startsWith("/register") || path.startsWith("/admin/login")) {
				return;
			}

			Cookie[] cookies = request.getCookies();
			if (cookies == null || cookies.length == 0) {
				return;
			}

			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(JwtRule.JWT_ISSUE_HEADER.getValue())) {
					cookieHeaders.append(cookie.getName() + "=" + cookie.getValue());
					break;
				}
			}

			if (cookieHeaders.toString().isEmpty()) {
				throw new LoginRedirectException("로그인을 다시 해주세요");
			}

			log.info("FeignCookieInterceptor:{}", cookieHeaders.toString());

			requestTemplate.header(JwtRule.JWT_ISSUE_HEADER.getValue(), cookieHeaders.toString());
			return;
		}

		String o = (String)request.getAttribute("access-refresh");
		String data = "";
		if (Objects.nonNull(o)) {
			data = o;
		} else {
			data = newCookie.get().getValue();
		}

		cookieHeaders.append(JwtRule.JWT_ISSUE_HEADER.getValue() + "=" + data);
		requestTemplate.header(JwtRule.JWT_ISSUE_HEADER.getValue(), cookieHeaders.toString());

	}

}
