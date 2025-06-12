package com.nhnacademy.front.jwt.parser;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.nhnacademy.front.account.member.exception.NotFoundMemberIdException;
import com.nhnacademy.front.jwt.rule.JwtRule;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

class JwtGetMemberIdTest {

	@Test
	@DisplayName("jwtGetMemberId 쿠키가 null 인 경우 테스트")
	void jwtGetMemberIdCookieNullTest() throws Exception {

		// Given
		HttpServletRequest request = mock(HttpServletRequest.class);

		// When
		when(request.getCookies()).thenReturn(null);

		// Then
		String result = JwtGetMemberId.jwtGetMemberId(request);
		assertThat(result).isNull();

	}

	@Test
	@DisplayName("jwtGetMemberId 쿠키가 없는 경우 테스트")
	void jwtGetMemberIdCookieEmptyTest() throws Exception {

		// Given
		HttpServletRequest request = mock(HttpServletRequest.class);
		Cookie[] cookies = {new Cookie("other", "value")};

		// When
		when(request.getCookies()).thenReturn(cookies);

		// Then
		String result = JwtGetMemberId.jwtGetMemberId(request);
		assertThat(result).isNull();

	}

	@Test
	@DisplayName("jwtGetMemberId memberId 파싱 실패 NotFoundMemberIdException 테스트")
	void jwtGetMemberIdNotFoundMemberIdExceptionTest() throws Exception {

		// Given
		HttpServletRequest request = mock(HttpServletRequest.class);
		Cookie[] cookies = {new Cookie(JwtRule.JWT_ISSUE_HEADER.getValue(), "header.payload.signature")};

		// When
		when(request.getCookies()).thenReturn(cookies);

		try (MockedStatic<JwtMemberIdParser> mockedStatic = mockStatic(JwtMemberIdParser.class)) {
			mockedStatic.when(() -> JwtMemberIdParser.getMemberId("header.payload.signature"))
				.thenReturn(null);

			Assertions.assertThrows(NotFoundMemberIdException.class, () -> {
				JwtGetMemberId.jwtGetMemberId(request);
			});
		}

	}

	@Test
	@DisplayName("jwtGetMemberId 메서드 테스트")
	void jwtGetMemberIdMethodTest() throws Exception {

		// Given
		HttpServletRequest request = mock(HttpServletRequest.class);
		Cookie[] cookies = {new Cookie(JwtRule.JWT_ISSUE_HEADER.getValue(), "header.payload.signature")};

		// When
		when(request.getCookies()).thenReturn(cookies);

		// Then
		try (MockedStatic<JwtMemberIdParser> mockedStatic = mockStatic(JwtMemberIdParser.class)) {
			mockedStatic.when(() -> JwtMemberIdParser.getMemberId("header.payload.signature"))
				.thenReturn("user");

			String result = JwtGetMemberId.jwtGetMemberId(request);
			assertThat(result).isEqualTo("user");
		}

	}
}