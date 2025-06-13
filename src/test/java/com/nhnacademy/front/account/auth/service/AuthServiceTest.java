package com.nhnacademy.front.account.auth.service;

import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.account.auth.adaptor.AuthAdaptor;
import com.nhnacademy.front.account.auth.exception.SaveJwtTokenProcessException;
import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.model.dto.response.ResponseJwtTokenDTO;
import com.nhnacademy.front.jwt.rule.JwtRule;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class AuthServiceTest {

	@InjectMocks
	private AuthService authService;

	@Mock
	private AuthAdaptor authAdaptor;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("JWT 토큰 생성 성공 및 쿠키 저장 테스트")
	void postAuthCreateJwtTokenMethodTest() {

		// Given
		RequestJwtTokenDTO requestDto = new RequestJwtTokenDTO("user");
		HttpHeaders headers = new HttpHeaders();
		headers.put(JwtRule.JWT_ISSUE_HEADER.getValue(),
			Collections.singletonList("Authorization=access.token.value; Path=/;"));

		ResponseJwtTokenDTO responseJwtTokenDTO = new ResponseJwtTokenDTO();
		ResponseEntity<ResponseJwtTokenDTO> responseEntity = ResponseEntity.ok()
			.headers(headers)
			.body(responseJwtTokenDTO);

		when(authAdaptor.postAuth(requestDto)).thenReturn(responseEntity);

		// When
		authService.postAuthCreateJwtToken(requestDto, response, request);

		// Then
		verify(response, times(2)).addCookie(any(Cookie.class));
		verify(request).setAttribute("access-refresh", "access.token.value");

	}

	@Test
	@DisplayName("JWT 토큰 생성 성공 및 쿠키 저장 SaveJwtTokenProcessException 테스트")
	void postAuthCreateJwtTokenSaveJwtTokenProcessExceptionTest() {

		// Given
		RequestJwtTokenDTO requestJwtTokenDTO = new RequestJwtTokenDTO("user");

		// When
		when(authAdaptor.postAuth(requestJwtTokenDTO)).thenReturn(null);

		// Then
		Assertions.assertThrows(SaveJwtTokenProcessException.class, () -> {
			authService.postAuthCreateJwtToken(requestJwtTokenDTO, response, request);
		});

	}

	@Test
	@DisplayName("JWT 토큰 생성 성공 및 쿠키 저장 SaveJwtTokenProcessException 테스트")
	void postAuthCreateJwtTokenFeignExceptionTest() {
		// given
		RequestJwtTokenDTO requestJwtTokenDTO = new RequestJwtTokenDTO("user");
		when(authAdaptor.postAuth(requestJwtTokenDTO)).thenThrow(mock(feign.FeignException.class));

		// then
		Assertions.assertThrows(SaveJwtTokenProcessException.class, () -> {
			authService.postAuthCreateJwtToken(requestJwtTokenDTO, response, request);
		});
	}

}