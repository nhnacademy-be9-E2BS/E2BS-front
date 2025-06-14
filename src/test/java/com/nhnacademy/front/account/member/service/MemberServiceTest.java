package com.nhnacademy.front.account.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nhnacademy.front.account.customer.model.domain.Customer;
import com.nhnacademy.front.account.member.adaptor.MemberInfoAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberRegisterAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberStateAdaptor;
import com.nhnacademy.front.account.member.exception.GetMemberStateFailedException;
import com.nhnacademy.front.account.member.exception.RegisterNotEqualsPasswordException;
import com.nhnacademy.front.account.member.exception.RegisterProcessException;
import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberStateDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseRegisterMemberDTO;
import com.nhnacademy.front.account.memberrank.model.domain.MemberRank;
import com.nhnacademy.front.account.memberrank.model.domain.RankName;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRole;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRoleName;
import com.nhnacademy.front.account.memberstate.model.domain.MemberState;
import com.nhnacademy.front.account.memberstate.model.domain.MemberStateName;
import com.nhnacademy.front.account.socialauth.model.domain.SocialAuth;
import com.nhnacademy.front.account.socialauth.model.domain.SocialAuthName;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRegisterAdaptor memberRegisterAdaptor;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private MemberInfoAdaptor memberInfoAdaptor;

	@Mock
	private MemberStateAdaptor memberStateAdaptor;

	@Test
	@DisplayName("회원가입 수행하는 메서드 테스트")
	void createMemberMethodTest() {

		// Given
		RequestRegisterMemberDTO requestRegisterMemberDTO = new RequestRegisterMemberDTO(
			"user",
			"user",
			"1234",
			"1234",
			"user@naver.com",
			LocalDate.now(),
			"010-1234-5678"
		);
		ResponseRegisterMemberDTO responseRegisterMemberDTO = new ResponseRegisterMemberDTO(
			"user", "user", "1234", "1234", LocalDate.now(), "010-1234-5678"
		);

		// When
		when(memberRegisterAdaptor.postRegisterMember(requestRegisterMemberDTO)).thenReturn(responseRegisterMemberDTO);

		// Then
		Assertions.assertThatNoException().isThrownBy(() -> memberService.createMember(requestRegisterMemberDTO));

	}

	@Test
	@DisplayName("회원가입 수행하는 메서드 RegisterNotEqualsPasswordException 테스트")
	void createMemberMethodRegisterNotEqualsPasswordExceptionTest() {

		// Given
		RequestRegisterMemberDTO requestRegisterMemberDTO = new RequestRegisterMemberDTO(
			"user",
			"user",
			"1234",
			"12345",
			"user@naver.com",
			LocalDate.now(),
			"010-1234-5678"
		);

		// When

		// Then
		assertThrows(RegisterNotEqualsPasswordException.class, () -> memberService.createMember(requestRegisterMemberDTO));

	}

	@Test
	@DisplayName("회원가입 수행하는 메서드 RegisterProcessException 테스트")
	void createMemberMethodRegisterProcessExceptionTest() {

		// Given
		RequestRegisterMemberDTO requestRegisterMemberDTO = new RequestRegisterMemberDTO(
			"user",
			"user",
			"1234",
			"1234",
			"user@naver.com",
			LocalDate.now(),
			"010-1234-5678"
		);

		// When
		when(memberRegisterAdaptor.postRegisterMember(requestRegisterMemberDTO)).thenReturn(null);

		// Then
		assertThrows(RegisterProcessException.class, () -> memberService.createMember(requestRegisterMemberDTO));

	}

	@Test
	@DisplayName("회원가입 수행하는 메서드 FeignException 테스트")
	void createMemberMethodFeignExceptionTest() {

		// Given
		RequestRegisterMemberDTO requestRegisterMemberDTO = new RequestRegisterMemberDTO(
			"user",
			"user",
			"1234",
			"1234",
			"user@naver.com",
			LocalDate.now(),
			"010-1234-5678"
		);

		// When
		when(memberRegisterAdaptor.postRegisterMember(requestRegisterMemberDTO)).thenThrow(FeignException.class);

		// Then
		assertThrows(RegisterProcessException.class, () -> memberService.createMember(requestRegisterMemberDTO));

	}

	@Test
	@DisplayName("회원 이름 조회하는 메서드 테스트")
	void getMemberNameMethodTest() {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			ResponseMemberInfoDTO responseMemberInfoDTO = new ResponseMemberInfoDTO(
				new Customer(),
				"user",
				LocalDate.now(),
				"01012345678",
				LocalDate.now(),
				LocalDate.now(),
				new MemberRank(1, RankName.NORMAL, 1, 1L),
				new MemberState(1, MemberStateName.ACTIVE),
				new MemberRole(1, MemberRoleName.MEMBER),
				new SocialAuth(1, SocialAuthName.WEB)
			);
			ResponseEntity<ResponseMemberInfoDTO> response = new ResponseEntity<>(responseMemberInfoDTO,
				HttpStatus.CREATED);

			// When
			when(memberInfoAdaptor.getMemberInfo("user")).thenReturn(response);

			String result = memberService.getMemberName(any(HttpServletRequest.class));

			// Then
			Assertions.assertThat(result).isEqualTo(memberService.getMemberName(any(HttpServletRequest.class)));

		}
	}

	@Test
	@DisplayName("회원 이름 null 인 경우 조회하는 메서드 테스트")
	void getMemberNameNullMethodTest() {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			ResponseMemberInfoDTO responseMemberInfoDTO = new ResponseMemberInfoDTO(
				new Customer(),
				"user",
				LocalDate.now(),
				"01012345678",
				LocalDate.now(),
				LocalDate.now(),
				new MemberRank(1, RankName.NORMAL, 1, 1L),
				new MemberState(1, MemberStateName.ACTIVE),
				new MemberRole(1, MemberRoleName.MEMBER),
				new SocialAuth(1, SocialAuthName.WEB)
			);
			ResponseEntity<ResponseMemberInfoDTO> response = new ResponseEntity<>(responseMemberInfoDTO,
				HttpStatus.BAD_REQUEST);

			// When
			when(memberInfoAdaptor.getMemberInfo("user")).thenReturn(response);

			String result = memberService.getMemberName(any(HttpServletRequest.class));

			// Then
			org.junit.jupiter.api.Assertions.assertNull(result);
		}
	}

	@Test
	@DisplayName("회원 상태 조회 메서드 테스트")
	void getMemberStateMethodTest() {

		// Given
		ResponseMemberStateDTO responseMemberStateDTO = new ResponseMemberStateDTO(
			"ACTIVE"
		);
		ResponseEntity<ResponseMemberStateDTO> response = new ResponseEntity<>(responseMemberStateDTO,
			HttpStatus.CREATED);

		// When
		when(memberStateAdaptor.getMemberState("user")).thenReturn(response);

		String result = memberService.getMemberState("user");

		// Then
		Assertions.assertThat(result).isEqualTo(response.getBody().getMemberstate());

	}

	@Test
	@DisplayName("회원 상태 조회 메서드 GetMemberStateFailedException 테스트")
	void getMemberStateMethodGetMemberStateFailedExceptionTest() {

		// Given
		ResponseMemberStateDTO responseMemberStateDTO = new ResponseMemberStateDTO(
			"ACTIVE"
		);
		ResponseEntity<ResponseMemberStateDTO> response = new ResponseEntity<>(responseMemberStateDTO,
			HttpStatus.BAD_REQUEST);

		// When
		when(memberStateAdaptor.getMemberState("user")).thenReturn(response);

		// Then
		assertThrows(GetMemberStateFailedException.class, () -> memberService.getMemberState("user"));

	}

	@Test
	@DisplayName("회원 역할 조회 메서드 테스트")
	void getMemberRoleMethodTest() {

		// Given
		ResponseMemberStateDTO responseMemberStateDTO = new ResponseMemberStateDTO(
			"ACTIVE"
		);
		ResponseEntity<ResponseMemberStateDTO> response = new ResponseEntity<>(responseMemberStateDTO,
			HttpStatus.CREATED);

		// When
		when(memberStateAdaptor.getMemberState("user")).thenReturn(response);

		String result = memberService.getMemberState("user");

		// Then
		Assertions.assertThat(result).isEqualTo(response.getBody().getMemberstate());

	}

	@Test
	@DisplayName("회원 역할 조회 실패 - 응답 코드가 2xx가 아님 테스트")
	void getMemberRoleMethodTestNotSuccessfulStatus() {
		// given
		String memberId = "user123";
		ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MEMBER");

		when(memberStateAdaptor.getMemberRole(memberId)).thenReturn(response);

		// when & then
		assertThrows(GetMemberStateFailedException.class,
			() -> memberService.getMemberRole(memberId));
	}

	@Test
	@DisplayName("회원 역할 조회 실패 - 응답 바디가 null 테스트")
	void getMemberRole_nullBody() {
		// given
		String memberId = "user123";
		ResponseEntity<String> response = ResponseEntity.ok(null);

		when(memberStateAdaptor.getMemberRole(memberId)).thenReturn(response);

		// when & then
		assertThrows(GetMemberStateFailedException.class,
			() -> memberService.getMemberRole(memberId));
	}

	@Test
	@DisplayName("회원 역할 조회 실패 - FeignException 테스트")
	void getMemberRole_feignException() {
		// given
		String memberId = "user123";

		when(memberStateAdaptor.getMemberRole(memberId))
			.thenThrow(mock(FeignException.class));

		// when & then
		assertThrows(GetMemberStateFailedException.class,
			() -> memberService.getMemberRole(memberId));
	}

}