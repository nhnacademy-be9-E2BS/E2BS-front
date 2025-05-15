/*
package com.nhnacademy.front.account.member.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.nhnacademy.front.account.member.adaptor.MemberLoginAdaptor;
import com.nhnacademy.front.account.member.exception.LoginProcessException;
import com.nhnacademy.front.account.member.model.domain.MemberRoleName;
import com.nhnacademy.front.account.member.model.dto.request.RequestLoginMemberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseLoginMemberDTO;

@ExtendWith(MockitoExtension.class)
class MemberLoginUserDetailsServiceTest {

	@InjectMocks
	private MemberLoginUserDetailsService memberLoginUserDetailsService;

	@Mock
	private MemberLoginAdaptor memberLoginAdaptor;

	@Test
	@DisplayName("정상적인 로그인 시 UserDetails 반환")
	void successLoginReturnUserDetailsTest() {

		// Given
		String memberId = "nhn1";
		String memberPassword = "1234";
		MemberRoleName memberRoleName = MemberRoleName.MEMBER;

		ResponseLoginMemberDTO responseLoginMemberDTO = new ResponseLoginMemberDTO(memberId, memberPassword,
			memberRoleName);

		when(memberLoginAdaptor.postLoginMember(any(RequestLoginMemberDTO.class)))
			.thenReturn(responseLoginMemberDTO);

		// When
		UserDetails userDetails = memberLoginUserDetailsService.loadUserByUsername(memberId);

		// Then
		Assertions.assertAll(() -> {
			Assertions.assertEquals(memberId, userDetails.getUsername());
			Assertions.assertEquals(memberPassword, userDetails.getPassword());
		});

	}

	@Test
	@DisplayName("사용자 정보 불일치 시 예외 처리")
	void failLoginReturnExceptionTest() {

		// Given
		String memberId = "nhn1";
		String memberPassword = "1234";
		MemberRoleName memberRoleName = MemberRoleName.MEMBER;

		ResponseLoginMemberDTO responseLoginMemberDTO = new ResponseLoginMemberDTO(memberId, null, memberRoleName);

		when(memberLoginAdaptor.postLoginMember(any(RequestLoginMemberDTO.class)))
			.thenReturn(responseLoginMemberDTO);

		// When

		// Then
		Assertions.assertThrows(LoginProcessException.class, () -> {
			memberLoginUserDetailsService.loadUserByUsername(memberId);
		});

	}

}

 */