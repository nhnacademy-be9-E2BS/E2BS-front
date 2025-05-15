/*
package com.nhnacademy.front.account.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nhnacademy.front.account.member.adaptor.MemberRegisterAdaptor;
import com.nhnacademy.front.account.member.exception.RegisterNotEqualsPasswordException;
import com.nhnacademy.front.account.member.exception.RegisterProcessException;
import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseRegisterMemberDTO;
import com.nhnacademy.front.common.exception.EmptyRequestException;

import feign.FeignException;
import feign.Request;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRegisterAdaptor memberRegisterAdaptor;

	@Test
	@DisplayName("createMember 메소드 성공")
	void successCreateMemberMethodTest() throws Exception {

		// Given
		String memberId = "nhn1";
		String customerName = "HNHACADEMY";
		String customerPassword = "1234";
		String customerPasswordCheck = "1234";
		String customerEmail = "dooray@gmail.com";
		LocalDate memberBirth = LocalDate.now();
		String memberPhone = "01012345678";

		RequestRegisterMemberDTO requestRegisterMemberDTO = new RequestRegisterMemberDTO(
			memberId, customerName, customerPassword, customerPasswordCheck, customerEmail, memberBirth, memberPhone
		);

		// When
		ResponseRegisterMemberDTO responseRegisterMemberDTO = new ResponseRegisterMemberDTO(
			memberId, customerName, customerPassword, customerEmail, memberBirth, memberPhone
		);

		when(memberRegisterAdaptor.postRegisterMember(requestRegisterMemberDTO)).thenReturn(responseRegisterMemberDTO);

		// Then
		assertThatCode(() -> memberService.createMember(requestRegisterMemberDTO))
			.doesNotThrowAnyException();

	}

	@Test
	@DisplayName("RequestRegisterMemberDTO 값이 없으면 EmptyRequestException 발생 테스트")
	void requestRegisterMemberDTOisEmptyRequestExceptionTest() throws Exception {

		// Given
		RequestRegisterMemberDTO requestRegisterMemberDTO = null;

		// When

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(EmptyRequestException.class, () -> {
			memberService.createMember(requestRegisterMemberDTO);
		});

	}

	@Test
	@DisplayName("customerPassword와 customerPasswordCheck 값이 다른 경우 예외 처리")
	void customerPasswordIsNotEqualsCustomerPasswordCheckTest() throws Exception {

		// Given
		String memberId = "nhn1";
		String customerName = "HNHACADEMY";
		String customerPassword = "1234";
		String customerPasswordCheck = "1234512341234";
		String customerEmail = "dooray@gmail.com";
		LocalDate memberBirth = LocalDate.now();
		String memberPhone = "01012345678";

		RequestRegisterMemberDTO requestRegisterMemberDTO = new RequestRegisterMemberDTO(
			memberId, customerName, customerPassword, customerPasswordCheck, customerEmail,
			memberBirth, memberPhone
		);

		// When

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(RegisterNotEqualsPasswordException.class, () -> {
			memberService.createMember(requestRegisterMemberDTO);
		});

	}

	@Test
	@DisplayName("회원 가입 실패 예외 처리")
	void failRegisterProcessExceptionTest() throws Exception {

		// Given
		String memberId = "nhn1";
		String customerName = "HNHACADEMY";
		String customerPassword = "1234";
		String customerPasswordCheck = "1234";
		String customerEmail = "dooray@gmail.com";
		LocalDate memberBirth = LocalDate.now();
		String memberPhone = "01012345678";

		RequestRegisterMemberDTO requestRegisterMemberDTO = new RequestRegisterMemberDTO(
			memberId, customerName, customerPassword, customerPasswordCheck, customerEmail,
			memberBirth, memberPhone
		);

		// When
		FeignException forbiddenException = FeignException.errorStatus(
			"postRegisterMember",
			feign.Response.builder()
				.status(403)
				.reason("Forbidden")
				.request(Request.create(Request.HttpMethod.POST, "/api/register", Map.of(), null, null, null))
				.build()
		);

		// Then
		assertThatThrownBy(() -> memberService.createMember(requestRegisterMemberDTO))
			.isInstanceOf(RegisterProcessException.class);

	}

}

 */