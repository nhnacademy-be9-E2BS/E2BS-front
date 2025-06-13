package com.nhnacademy.front.account.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.nhnacademy.front.account.member.adaptor.DoorayAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberDormantAdaptor;
import com.nhnacademy.front.account.member.exception.DormantProcessingException;
import com.nhnacademy.front.account.member.model.dto.request.RequestDoorayAuthenticationDTO;
import com.nhnacademy.front.account.member.model.dto.request.RequestDormantDoorayNumberDTO;
import com.nhnacademy.front.account.member.model.dto.request.RequestDormantEmailNumberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberEmailDTO;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class MemberDormantServiceTest {

	@InjectMocks
	private MemberDormantService memberDormantService;

	@Mock
	private RedisTemplate<String, Object> redisTemplate;

	@Mock
	private MemberDormantAdaptor memberDormantAdaptor;

	@Mock
	private DoorayAdaptor doorayAdaptor;

	@Mock
	private JavaMailSender javaMailSender;

	@Mock
	private ValueOperations<String, Object> valueOperations;

	@Test
	@DisplayName("Dooray 인증 번호 전송 메서드 테스트")
	void createDoorayAuthenticationNumberMethodTest()  {

		// Given
		ValueOperations<String, Object> valueOperationsGiven = mock(ValueOperations.class);

		when(redisTemplate.opsForValue()).thenReturn(valueOperationsGiven);

		// When

		// Then
		Assertions.assertThatCode(() -> {
			memberDormantService.createDoorayAuthenticationNumber("user");
		}).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("Email 인증 번호 전송 메서드 테스트")
	void createEmailAuthenticationNumberMethodTest() {

		// Given
		ValueOperations<String, Object> valueOperationsGiven = mock(ValueOperations.class);

		when(redisTemplate.opsForValue()).thenReturn(valueOperationsGiven);

		// When

		// Then
		Assertions.assertThatCode(() -> {
			memberDormantService.createEmailAuthenticationNumber("user");
		}).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("Dooray 인증번호 확인 메서드 테스트")
	void checkDoorayAuthenticationNumberMethodTest() {

		// Given
		String doorayDormantKey = "doorayDormantNumber:user";
		RequestDormantDoorayNumberDTO requestDormantDoorayNumberDTO = new RequestDormantDoorayNumberDTO(
			"123456"
		);

		// When
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(doorayDormantKey)).thenReturn(
			123456
		);

		// Then
		Assertions.assertThatCode(() -> {
			memberDormantService.checkDoorayAuthenticationNumber(requestDormantDoorayNumberDTO, "user");
		}).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Dooray 인증번호 확인 메서드 DormantProcessingException 테스트")
	void checkDoorayAuthenticationNumberMethodDormantProcessingExceptionTest() {

		// Given
		String doorayDormantKey = "doorayDormantNumber:user";
		RequestDormantDoorayNumberDTO requestDormantDoorayNumberDTO = new RequestDormantDoorayNumberDTO(
			"000000"
		);

		// When
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(doorayDormantKey)).thenReturn(null);

		// Then
		assertThrows(DormantProcessingException.class, () -> {
			memberDormantService.checkDoorayAuthenticationNumber(requestDormantDoorayNumberDTO, "user");
		});

	}

	@Test
	@DisplayName("Email 인증번호 확인 메서드 테스트")
	void checkEmailAuthenticationNumberMethodTest() {

		// Given
		String emailDormantKey = "emailDormantNumber:user";
		RequestDormantEmailNumberDTO requestDormantEmailNumberDTO = new RequestDormantEmailNumberDTO(
			"000000"
		);

		// When
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(emailDormantKey)).thenReturn(123456);

		// Then
		Assertions.assertThatCode(() -> {
			memberDormantService.checkEmailAuthenticationNumber(requestDormantEmailNumberDTO, "user");
		}).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Email 인증번호 확인 메서드 DormantProcessingException 테스트")
	void checkEmailAuthenticationNumberMethodDormantProcessingExceptionTest() {

		// Given
		String emailDormantKey = "emailDormantNumber:user";
		RequestDormantEmailNumberDTO requestDormantEmailNumberDTO = new RequestDormantEmailNumberDTO(
			"000000"
		);

		// When
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(emailDormantKey)).thenReturn(null);

		// Then
		assertThrows(DormantProcessingException.class, () -> {
			memberDormantService.checkEmailAuthenticationNumber(requestDormantEmailNumberDTO, "user");
		});

	}

	@Test
	@DisplayName("Dooray 인증 번호 메세지 전송 메서드 테스트")
	void sendDoorayMessageAuthenticationNumberMethodTest() {

		// Given
		RequestDoorayAuthenticationDTO requestDoorayAuthenticationDTO = new RequestDoorayAuthenticationDTO(
			"관리자", "인증번호"
		);

		// When
		when(doorayAdaptor.sendDoorayAuthenticationNumber(requestDoorayAuthenticationDTO,
			"3204376758577275363", "4081630330794795988", "b42JstgjQf-uqLBb3dj_yg")).thenReturn("good");

		// Then
		memberDormantService.sendDoorayMessageAuthenticationNumber(requestDoorayAuthenticationDTO);
		verify(doorayAdaptor).sendDoorayAuthenticationNumber(
			requestDoorayAuthenticationDTO,
			"3204376758577275363",
			"4081630330794795988",
			"b42JstgjQf-uqLBb3dj_yg"
		);
	}

	@Test
	@DisplayName("회원 상태 Active 변경하는 메서드 테스트")
	void changeMemberStateActiveMethodTest() {

		// Given
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", "user");
		session.setAttribute("memberState", "ACTIVE");

		request.setSession(session);

		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CREATED);

		// When
		when(memberDormantAdaptor.changeDormantMemberStateActive("user")).thenReturn(response);

		// Then
		Assertions.assertThatCode(() -> {
			memberDormantService.changeMemberStateActive("user", request);
		}).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("회원 상태 Active 변경하는 메서드 DormantProcessingException 테스트")
	void changeMemberStateActiveMethodDormantProcessingExceptionTest() {

		// Given
		ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		HttpServletRequest request = mock(HttpServletRequest.class);

		// When
		when(memberDormantAdaptor.changeDormantMemberStateActive("user")).thenReturn(response);

		// Then
		assertThrows(DormantProcessingException.class, () ->
			memberDormantService.changeMemberStateActive("user", request)
		);

	}

	@Test
	@DisplayName("회원의 이메일을 가져오는 메서드 테스트")
	void getMemberEmailMethodTest() {

		// Given
		ResponseMemberEmailDTO responseMemberEmailDTO = new ResponseMemberEmailDTO(
			"user@naver.com"
		);
		ResponseEntity<ResponseMemberEmailDTO> response = new ResponseEntity<>(responseMemberEmailDTO,
			HttpStatus.CREATED);

		// When
		when(memberDormantAdaptor.getMemberEmail("user")).thenReturn(response);

		String result = memberDormantService.getMemberEmail("user");

		// Then
		Assertions.assertThat(result).isEqualTo(memberDormantService.getMemberEmail("user"));

	}

	@Test
	@DisplayName("회원의 이메일을 가져오는 메서드 DormantProcessingException 테스트")
	void getMemberEmailMethodDormantProcessingExceptionTest() {

		// Given
		ResponseMemberEmailDTO responseMemberEmailDTO = new ResponseMemberEmailDTO(
			"user@naver.com"
		);
		ResponseEntity<ResponseMemberEmailDTO> response = new ResponseEntity<>(responseMemberEmailDTO,
			HttpStatus.BAD_REQUEST);

		// When
		when(memberDormantAdaptor.getMemberEmail("user")).thenReturn(response);

		// Then
		assertThrows(DormantProcessingException.class, () -> {
			memberDormantService.getMemberEmail("user");
		});

	}

	@Test
	@DisplayName("해당 회원에게 이메일 전송하는 메서드 테스트")
	void sendEmailAuthenticationNumberMethodTest() {

		// Given

		// When
		doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

		// Then
		Assertions.assertThatCode(() -> {
			memberDormantService.sendEmailAuthenticationNumber("user@naver.com", "000000");
		}).doesNotThrowAnyException();

	}

}