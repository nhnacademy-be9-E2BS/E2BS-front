package com.nhnacademy.front.account.oauth.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.service.AuthService;
import com.nhnacademy.front.account.oauth.adaptor.OAuthLoginAdaptor;
import com.nhnacademy.front.account.oauth.adaptor.OAuthPaycoMemberInfoAdaptor;
import com.nhnacademy.front.account.oauth.adaptor.OAuthProviderPaycoAccessTokenAdaptor;
import com.nhnacademy.front.account.oauth.adaptor.OAuthRegisterAdaptor;
import com.nhnacademy.front.account.oauth.exception.PaycoProcessingException;
import com.nhnacademy.front.account.oauth.model.dto.request.RequestOAuthRegisterDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseCheckOAuthIdDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponsePaycoMemberInfoDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseProviderPaycoAccessTokenDTO;
import com.nhnacademy.front.cart.service.CartService;
import com.nhnacademy.front.cart.service.MemberCartService;
import com.nhnacademy.front.common.error.exception.ServerErrorException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith({MockitoExtension.class})
class OAuthServiceTest {

	@InjectMocks
	private OAuthService oAuthService;

	@Mock
	private OAuthProviderPaycoAccessTokenAdaptor oAuthProviderPaycoAccessTokenAdaptor;

	@Mock
	private OAuthPaycoMemberInfoAdaptor oAuthPaycoMemberInfoAdaptor;

	@Mock
	private OAuthLoginAdaptor oAuthLoginAdaptor;

	@Mock
	private OAuthRegisterAdaptor oAuthRegisterAdaptor;

	@Mock
	private AuthService authService;

	@Mock
	private MemberCartService memberCartService;

	@Mock
	private CartService cartService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Test
	@DisplayName("Payco AccessToken 정상 응답 테스트")
	void getPaycoAccessTokenMethodTest() {

		// Given
		String dummyCode = "dummy-code";
		ResponseProviderPaycoAccessTokenDTO dummyTokenDTO = new ResponseProviderPaycoAccessTokenDTO();
		ResponseEntity<ResponseProviderPaycoAccessTokenDTO> responseEntity =
			new ResponseEntity<>(dummyTokenDTO, HttpStatus.CREATED);

		// When
		when(oAuthProviderPaycoAccessTokenAdaptor.getPaycoAccessToken(
			any(), any(), any(), eq(dummyCode)
		)).thenReturn(responseEntity);

		ReflectionTestUtils.setField(oAuthService, "authorizationGrantType", "authorization_code");
		ReflectionTestUtils.setField(oAuthService, "clientId", "test-client-id");
		ReflectionTestUtils.setField(oAuthService, "clientSecret", "test-secret");

		ResponseProviderPaycoAccessTokenDTO result = oAuthService.getPaycoAccessToken(dummyCode);

		// Then
		Assertions.assertThat(result).isNotNull();
	}

	@Test
	@DisplayName("Payco AccessToken PaycoProcessingException 응답 테스트")
	void getPaycoAccessTokenMethodPaycoProcessingExceptionTest() {

		// Given
		String dummyCode = "dummy-code";
		ResponseProviderPaycoAccessTokenDTO dummyTokenDTO = new ResponseProviderPaycoAccessTokenDTO();
		ResponseEntity<ResponseProviderPaycoAccessTokenDTO> responseEntity =
			new ResponseEntity<>(dummyTokenDTO, HttpStatus.BAD_REQUEST);

		// When
		when(oAuthProviderPaycoAccessTokenAdaptor.getPaycoAccessToken(
			any(), any(), any(), eq(dummyCode)
		)).thenReturn(responseEntity);

		ReflectionTestUtils.setField(oAuthService, "authorizationGrantType", "authorization_code");
		ReflectionTestUtils.setField(oAuthService, "clientId", "test-client-id");
		ReflectionTestUtils.setField(oAuthService, "clientSecret", "test-secret");

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(PaycoProcessingException.class, () -> oAuthService.getPaycoAccessToken(dummyCode));
	}

	@Test
	@DisplayName("Payco 회원 정보 메서드 테스트")
	void getPaycoMemberInfoMethodTest() {

		// Given
		ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO = new ResponsePaycoMemberInfoDTO();
		ResponsePaycoMemberInfoDTO.PaycoMember member = new ResponsePaycoMemberInfoDTO.PaycoMember();
		member.setIdNo("idNo");
		member.setEmail("email");
		member.setMobile("01012345678");
		member.setName("홍길동");
		member.setBirthdayMMdd("0101");

		ResponsePaycoMemberInfoDTO.PaycoData data = new ResponsePaycoMemberInfoDTO.PaycoData();
		data.setMember(member);
		responsePaycoMemberInfoDTO.setData(data);

		ResponseEntity<ResponsePaycoMemberInfoDTO> responseResult = new ResponseEntity<>(responsePaycoMemberInfoDTO,
			HttpStatus.CREATED);

		// When
		when(oAuthPaycoMemberInfoAdaptor.getPaycoMemberInfo("user", "access")).thenReturn(responseResult);

		ReflectionTestUtils.setField(oAuthService, "clientId", "user");
		ResponsePaycoMemberInfoDTO result = oAuthService.getPaycoMemberInfo("access");

		// Then
		Assertions.assertThat(result).isEqualTo(responseResult.getBody());

	}

	@Test
	@DisplayName("Payco 회원 정보 메서드 PaycoProcessException 테스트")
	void getPaycoMemberInfoPaycoProcessExceptionTest() {

		// Given
		ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO = new ResponsePaycoMemberInfoDTO();
		ResponsePaycoMemberInfoDTO.PaycoMember member = new ResponsePaycoMemberInfoDTO.PaycoMember();
		member.setIdNo("idNo");
		member.setEmail("email");
		member.setMobile("01012345678");
		member.setName("홍길동");
		member.setBirthdayMMdd("0101");

		ResponsePaycoMemberInfoDTO.PaycoData data = new ResponsePaycoMemberInfoDTO.PaycoData();
		data.setMember(member);

		ResponseEntity<ResponsePaycoMemberInfoDTO> responseResult = new ResponseEntity<>(responsePaycoMemberInfoDTO,
			HttpStatus.CREATED);

		// When
		when(oAuthPaycoMemberInfoAdaptor.getPaycoMemberInfo("user", "access")).thenReturn(responseResult);

		ReflectionTestUtils.setField(oAuthService, "clientId", "user");

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(PaycoProcessingException.class, () -> oAuthService.getPaycoMemberInfo("access"));

	}

	@Test
	@DisplayName("Payco 로그인 메서드 테스트")
	void paycoLoginMethodTest() {

		// Given
		ResponsePaycoMemberInfoDTO.PaycoMember member = new ResponsePaycoMemberInfoDTO.PaycoMember();
		member.setIdNo("idNo");
		member.setEmail("email");
		member.setMobile("01012345678");
		member.setName("홍길동");
		member.setBirthdayMMdd("0101");

		ResponsePaycoMemberInfoDTO.PaycoData data = new ResponsePaycoMemberInfoDTO.PaycoData();
		data.setMember(member);

		ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO = new ResponsePaycoMemberInfoDTO();
		responsePaycoMemberInfoDTO.setData(data);

		ResponseCheckOAuthIdDTO responseCheckOAuthIdDTO = new ResponseCheckOAuthIdDTO(true);
		ResponseEntity<ResponseCheckOAuthIdDTO> responseCheck = new ResponseEntity<>(responseCheckOAuthIdDTO,
			HttpStatus.CREATED);
		ResponseEntity<Void> lastLoginResponse = new ResponseEntity<>(HttpStatus.CREATED);

		// When
		when(oAuthLoginAdaptor.checkOAuthLoginId("idNo")).thenReturn(responseCheck);
		doNothing().when(authService).postAuthCreateJwtToken(any(RequestJwtTokenDTO.class), eq(response), eq(request));
		when(oAuthLoginAdaptor.loginOAuthLastLogin("idNo")).thenReturn(lastLoginResponse);

		// Then
		Assertions.assertThatCode(() -> oAuthService.paycoLogin(responsePaycoMemberInfoDTO, request, response)).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("Payco 로그인 메서드 PaycoProcessingException 테스트")
	void paycoLoginMethodPaycoProcessingExceptionTest() {

		// Given
		ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO = new ResponsePaycoMemberInfoDTO();
		ResponsePaycoMemberInfoDTO.PaycoMember member = new ResponsePaycoMemberInfoDTO.PaycoMember();
		member.setIdNo("idNo");
		member.setEmail("email");
		member.setMobile("01012345678");
		member.setName("홍길동");
		member.setBirthdayMMdd("0101");

		ResponsePaycoMemberInfoDTO.PaycoData data = new ResponsePaycoMemberInfoDTO.PaycoData();
		data.setMember(member);
		responsePaycoMemberInfoDTO.setData(data);

		ResponseCheckOAuthIdDTO responseCheckOAuthIdDTO = new ResponseCheckOAuthIdDTO(true);
		ResponseEntity<ResponseCheckOAuthIdDTO> responseCheck = new ResponseEntity<>(responseCheckOAuthIdDTO,
			HttpStatus.BAD_REQUEST);

		// When
		when(oAuthLoginAdaptor.checkOAuthLoginId("idNo")).thenReturn(responseCheck);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(PaycoProcessingException.class, () -> oAuthService.paycoLogin(responsePaycoMemberInfoDTO, request, response));

	}

	@Test
	@DisplayName("Payco 회원가입 메서드 테스트")
	void paycoRegisterMethodTest() {

		// Given
		ResponsePaycoMemberInfoDTO.PaycoMember member = new ResponsePaycoMemberInfoDTO.PaycoMember();
		member.setIdNo("idNo");
		member.setEmail("email");
		member.setMobile("01012345678");
		member.setName("홍길동");
		member.setBirthdayMMdd("0101");

		ResponsePaycoMemberInfoDTO.PaycoData data = new ResponsePaycoMemberInfoDTO.PaycoData();
		data.setMember(member);

		ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO = new ResponsePaycoMemberInfoDTO();
		responsePaycoMemberInfoDTO.setData(data);

		ResponseCheckOAuthIdDTO responseCheckOAuthIdDTO = new ResponseCheckOAuthIdDTO(false);
		ResponseEntity<ResponseCheckOAuthIdDTO> responseCheck = new ResponseEntity<>(responseCheckOAuthIdDTO,
			HttpStatus.CREATED);

		ResponseEntity<Void> registerResponse = new ResponseEntity<>(HttpStatus.CREATED);

		// When
		when(oAuthLoginAdaptor.checkOAuthLoginId("idNo")).thenReturn(responseCheck);
		doNothing().when(authService).postAuthCreateJwtToken(any(RequestJwtTokenDTO.class), eq(response), eq(request));
		when(oAuthRegisterAdaptor.registerOAuth(any(RequestOAuthRegisterDTO.class))).thenReturn(registerResponse);
		when(oAuthLoginAdaptor.loginOAuthLastLogin("idNo")).thenReturn(registerResponse);

		// Then
		Assertions.assertThatCode(() -> oAuthService.paycoLogin(responsePaycoMemberInfoDTO, request, response)).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("Payco 회원가입 메서드 ServerErrorException 테스트")
	void paycoRegisterMethodServerErrorExceptionTest() {

		// Given
		ResponsePaycoMemberInfoDTO.PaycoMember member = new ResponsePaycoMemberInfoDTO.PaycoMember();
		member.setIdNo("idNo");
		member.setEmail("email");
		member.setMobile("01012345678");
		member.setName("홍길동");
		member.setBirthdayMMdd("0101");

		ResponsePaycoMemberInfoDTO.PaycoData data = new ResponsePaycoMemberInfoDTO.PaycoData();
		data.setMember(member);

		ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO = new ResponsePaycoMemberInfoDTO();
		responsePaycoMemberInfoDTO.setData(data);

		ResponseCheckOAuthIdDTO responseCheckOAuthIdDTO = new ResponseCheckOAuthIdDTO(false);
		ResponseEntity<ResponseCheckOAuthIdDTO> responseCheck = new ResponseEntity<>(responseCheckOAuthIdDTO,
			HttpStatus.CREATED);

		ResponseEntity<Void> registerResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		// When
		when(oAuthLoginAdaptor.checkOAuthLoginId("idNo")).thenReturn(responseCheck);
		when(oAuthRegisterAdaptor.registerOAuth(any(RequestOAuthRegisterDTO.class))).thenReturn(registerResponse);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(ServerErrorException.class, () -> oAuthService.paycoLogin(responsePaycoMemberInfoDTO, request, response));

	}

}