package com.nhnacademy.front.common.interceptor;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class CartInterceptorTest {

	private CartInterceptor interceptor;
	private HttpServletRequest request;
	private HttpServletResponse response;

	@BeforeEach
	void setUp() {
		interceptor = new CartInterceptor();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
	}

	@Test
	@DisplayName("쿠키에 orderCart가 있는 경우 제거 동작 테스트")
	void preHandle_OrderCartCookieExists() {
		// given
		when(request.getRequestURI()).thenReturn("/some/uri");
		Cookie targetCookie = new Cookie("orderCart", "some-value");
		Cookie[] cookies = {targetCookie};
		when(request.getCookies()).thenReturn(cookies);

		// when
		boolean result = interceptor.preHandle(request, response, new Object());

		// then
		assertTrue(result);
		assertThat(targetCookie.getValue()).isEmpty();
		assertEquals(0, targetCookie.getMaxAge());
		verify(response).addCookie(targetCookie);
	}

	@Test
	@DisplayName("쿠키에 orderCart가 없는 경우")
	void preHandle_OrderCartCookieNotFound() {
		// given
		when(request.getRequestURI()).thenReturn("/some/uri");
		Cookie[] cookies = {new Cookie("other", "value")};
		when(request.getCookies()).thenReturn(cookies);

		// when
		boolean result = interceptor.preHandle(request, response, new Object());

		// then
		assertTrue(result);
		verify(response, never()).addCookie(any());
	}

	@Test
	@DisplayName("요청 쿠키가 null인 경우")
	void preHandle_NoCookies() {
		// given
		when(request.getRequestURI()).thenReturn("/some/uri");
		when(request.getCookies()).thenReturn(null);

		// when
		boolean result = interceptor.preHandle(request, response, new Object());

		// then
		assertTrue(result);
		verify(response, never()).addCookie(any());
	}

	@ParameterizedTest
	@ValueSource(strings = {"/order", "/members/order", "/customers/order"})
	@DisplayName("특정 URI로 시작하는 경우 쿠키 제거하지 않음")
	void preHandle_OrderUri_NoCookieRemove(String uri) {
		// given
		when(request.getRequestURI()).thenReturn(uri);
		Cookie targetCookie = new Cookie("orderCart", "some-value");
		Cookie[] cookies = {targetCookie};
		when(request.getCookies()).thenReturn(cookies);

		// when
		boolean result = interceptor.preHandle(request, response, new Object());

		// then
		assertTrue(result);
		verify(response, never()).addCookie(any());
	}

}