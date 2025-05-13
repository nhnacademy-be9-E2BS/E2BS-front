package com.nhnacademy.front.common.interceptor;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

		Cookie[] cookies = request.getCookies();
		if (cookies.length <= 0) {
			return;
		}

		StringBuilder cookieHeaders = new StringBuilder();

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("Set-Cookie")) {
				cookieHeaders.append(cookie.getName() + "=" + cookie.getValue());
				break;
			}
		}

		requestTemplate.header("Set-Cookie", cookieHeaders.toString());
	}
}
