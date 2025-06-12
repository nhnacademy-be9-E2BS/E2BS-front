// package com.nhnacademy.front.account.auth.service;
//
// import static org.assertj.core.api.AssertionsForClassTypes.*;
// import static org.mockito.Mockito.*;
//
// import org.assertj.core.api.Assertions;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
//
// import com.nhnacademy.front.account.auth.adaptor.AuthAdaptor;
// import com.nhnacademy.front.account.auth.exception.SaveJwtTokenProcessException;
// import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
// import com.nhnacademy.front.account.auth.model.dto.response.ResponseJwtTokenDTO;
// import com.nhnacademy.front.jwt.rule.JwtRule;
//
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
//
// @ExtendWith({MockitoExtension.class})
// class AuthServiceTest {
//
// 	@InjectMocks
// 	private AuthService authService;
//
// 	@Mock
// 	private AuthAdaptor authAdaptor;
//
// 	@Mock
// 	private HttpServletRequest request;
//
// 	@Mock
// 	private HttpServletResponse response;
//
// 	@BeforeEach
// 	void setUp() {
// 		MockitoAnnotations.openMocks(this);
// 	}
//
// 	@Test
// 	@DisplayName("JWT 토큰 발급 및 쿠키 저장 성공")
// 	void testPostAuthCreateJwtToken_success() {
//
// 		// Given
// 		RequestJwtTokenDTO requestJwtTokenDTO = new RequestJwtTokenDTO("user");
// 		ResponseJwtTokenDTO responseJwtTokenDTO = new ResponseJwtTokenDTO("success");
//
// 		HttpHeaders headers = new HttpHeaders();
// 		headers.add(JwtRule.JWT_ISSUE_HEADER.getValue(), "access-token=abc.def.ghi; Path=/; Secure; HttpOnly");
//
// 		ResponseEntity<ResponseJwtTokenDTO> responseEntity = new ResponseEntity<>(responseJwtTokenDTO,
// 			headers, HttpStatus.CREATED);
//
// 		// When
// 		when(authAdaptor.postAuth(requestJwtTokenDTO)).thenReturn(responseEntity);
//
// 		// Then
// 		Assertions.assertThatCode(() -> {
// 			authService.postAuthCreateJwtToken(requestJwtTokenDTO, response, request);
// 		}).doesNotThrowAnyException();
//
// 	}
//
// 	@Test
// 	@DisplayName("JWT 토큰 응답이 null일 경우 예외 발생")
// 	void testPostAuthCreateJwtToken_responseNull() {
//
// 		// Given
// 		RequestJwtTokenDTO requestDto = new RequestJwtTokenDTO("user");
//
// 		// When
// 		when(authAdaptor.postAuth(any())).thenReturn(null);
//
// 		// Then
// 		assertThatThrownBy(() -> authService.postAuthCreateJwtToken(requestDto, response, request))
// 			.isInstanceOf(SaveJwtTokenProcessException.class);
//
// 	}
//
// 	@Test
// 	@DisplayName("Feign 예외 발생 시 예외 처리")
// 	void testPostAuthCreateJwtToken_feignException() {
//
// 		// Given
// 		RequestJwtTokenDTO requestDto = new RequestJwtTokenDTO("user");
//
// 		// When
// 		when(authAdaptor.postAuth(any())).thenThrow(mock(feign.FeignException.class));
//
// 		// Then
// 		assertThatThrownBy(() -> authService.postAuthCreateJwtToken(requestDto, response, request))
// 			.isInstanceOf(SaveJwtTokenProcessException.class);
//
// 	}
//
// }