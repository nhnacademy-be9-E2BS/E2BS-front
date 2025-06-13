package com.nhnacademy.front.common.handler;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;

import com.nhnacademy.front.jwt.parser.JwtMemberIdParser;
import com.nhnacademy.front.jwt.rule.JwtRule;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class CustomLogoutHandlerTest {

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private Authentication authentication;

	@Mock
	private HttpSession session;

	private CustomLogoutHandler logoutHandler;

	@BeforeEach
	void setUp() {
		logoutHandler = new CustomLogoutHandler(redisTemplate);
	}

	@Test
	@DisplayName("정상 로그아웃 처리 테스트 - 쿠키 삭제 및 Redis 키 제거")
	void logoutSuccessTest() {
		// given
		String tokenValue = "mock.jwt.token";
		String memberId = "user123";

		Cookie jwtCookie = new Cookie(JwtRule.JWT_ISSUE_HEADER.getValue(), tokenValue);
		Cookie[] cookies = new Cookie[] { jwtCookie };

		when(request.getCookies()).thenReturn(cookies);
		when(request.getSession()).thenReturn(session);

		try (MockedStatic<JwtMemberIdParser> parserMock = Mockito.mockStatic(JwtMemberIdParser.class)) {
			parserMock.when(() -> JwtMemberIdParser.getMemberId(tokenValue)).thenReturn(memberId);

			// when
			logoutHandler.logout(request, response, authentication);

			// then
			verify(redisTemplate).delete("refresh:" + memberId);
			verify(response).addCookie(argThat(cookie ->
				cookie.getName().equals(JwtRule.JWT_ISSUE_HEADER.getValue()) &&
					cookie.getValue() == null &&
					cookie.getMaxAge() == 0
			));
			verify(session).invalidate();
		}
	}

	@Test
	@DisplayName("JWT 쿠키 없을 경우 테스트 - 아무 작업 안 하고 세션만 만료")
	void logoutNoJwtCookieTest() {
		// given
		Cookie otherCookie = new Cookie("other", "value");
		when(request.getCookies()).thenReturn(new Cookie[] { otherCookie });
		when(request.getSession()).thenReturn(session);

		// when
		logoutHandler.logout(request, response, authentication);

		// then
		verify(redisTemplate, never()).delete(anyString());
		verify(response, never()).addCookie(any());
		verify(session).invalidate();
	}

	@Test
	@DisplayName("쿠키 배열 null - 세션만 만료")
	void logoutWithNoCookiesAtAll() {
		// given
		when(request.getCookies()).thenReturn(null);
		when(request.getSession()).thenReturn(session);

		// when
		logoutHandler.logout(request, response, authentication);

		// then
		verify(redisTemplate, never()).delete(anyString());
		verify(response, never()).addCookie(any());
		verify(session).invalidate();
	}

}
