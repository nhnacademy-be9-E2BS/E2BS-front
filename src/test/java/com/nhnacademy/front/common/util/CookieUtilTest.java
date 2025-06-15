package com.nhnacademy.front.common.util;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.http.Cookie;

class CookieUtilTest {

	private static final String COOKIE_NAME = "testCookie";
	private static final String COOKIE_VALUE = "12345";

	@Test
	@DisplayName("쿠키 설정 테스트")
	void setCookie() {
		// given
		MockHttpServletResponse response = new MockHttpServletResponse();

		// when
		CookieUtil.setCookie(COOKIE_NAME, response, COOKIE_VALUE);

		// then
		String setCookieHeader = response.getHeader("Set-Cookie");
		assertNotNull(setCookieHeader);
		assertThat(setCookieHeader).contains(COOKIE_NAME + "=" + COOKIE_VALUE);
		assertThat(setCookieHeader).contains("HttpOnly");
		assertThat(setCookieHeader).contains("Secure");
		assertThat(setCookieHeader).contains("SameSite=Lax");
		assertThat(setCookieHeader).contains("Max-Age=10800");
	}

	@Test
	@DisplayName("쿠키 값 조회 테스트")
	void getCookieValue_Found() {
		// given
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setCookies(new Cookie(COOKIE_NAME, COOKIE_VALUE));

		// when
		String value = CookieUtil.getCookieValue(COOKIE_NAME, request);

		// then
		assertThat(value).isEqualTo(COOKIE_VALUE);
	}

	@Test
	@DisplayName("쿠키 값 조회 테스트 - 실패(쿠키 값을 찾지 못한 경우)")
	void getCookieValue_Fail_NotFound() {
		// given
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setCookies(new Cookie("other", "value"));

		// when
		String value = CookieUtil.getCookieValue(COOKIE_NAME, request);

		// then
		assertThat(value).isNull();
	}

	@Test
	@DisplayName("쿠키 초기화 테스트")
	void clearCookie() {
		// given
		MockHttpServletResponse response = new MockHttpServletResponse();

		// when
		CookieUtil.clearCookie(COOKIE_NAME, response);

		// then
		String setCookieHeader = response.getHeader("Set-Cookie");
		assertThat(setCookieHeader).isNotNull();
		assertThat(setCookieHeader).contains(COOKIE_NAME + "=");
		assertThat(setCookieHeader).contains("Max-Age=0");
		assertThat(setCookieHeader).contains("SameSite=Lax");
		assertThat(setCookieHeader).contains("Secure");
		assertThat(setCookieHeader).contains("HttpOnly");
	}

}
