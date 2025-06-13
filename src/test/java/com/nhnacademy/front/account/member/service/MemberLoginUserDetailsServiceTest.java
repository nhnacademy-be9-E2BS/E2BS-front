package com.nhnacademy.front.account.member.service;

import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nhnacademy.front.account.member.adaptor.MemberLoginAdaptor;
import com.nhnacademy.front.account.member.exception.LoginProcessException;
import com.nhnacademy.front.account.member.model.dto.request.RequestLoginMemberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseLoginMemberDTO;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRoleName;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class MemberLoginUserDetailsServiceTest {

	@InjectMocks
	private MemberLoginUserDetailsService memberLoginUserDetailsService;

	@Mock
	private MemberLoginAdaptor memberLoginAdaptor;

	@Test
	@DisplayName("Security 로그인을 위한 UserDetailService 테스트")
	void memberLoginUserDetailsServiceTest() {

		// Given
		ResponseLoginMemberDTO responseLoginMemberDTO = new ResponseLoginMemberDTO(
			"user", "1234", MemberRoleName.MEMBER
		);

		// When
		when(memberLoginAdaptor.postLoginMember(any(RequestLoginMemberDTO.class))).thenReturn(responseLoginMemberDTO);

		// Then
		Assertions.assertThatCode(() -> {
			memberLoginUserDetailsService.loadUserByUsername("user");
		}).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("Security 로그인을 위한 UserDetailService LoginProcessException 테스트")
	void memberLoginUserDetailsServiceLoginProcessExceptionTest() {

		// Given
		ResponseLoginMemberDTO responseLoginMemberDTO = new ResponseLoginMemberDTO(
			"user", null, MemberRoleName.MEMBER
		);

		// When
		when(memberLoginAdaptor.postLoginMember(any(RequestLoginMemberDTO.class)))
			.thenReturn(responseLoginMemberDTO);

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(LoginProcessException.class, () -> {
			memberLoginUserDetailsService.loadUserByUsername("user");
		});

	}

	@Test
	@DisplayName("Security 로그인을 위한 UserDetailService FeignException 테스트")
	void memberLoginUserDetailsServiceFeignExceptionTest() {

		// Given

		// When
		when(memberLoginAdaptor.postLoginMember(any(RequestLoginMemberDTO.class))).thenThrow(
			mock(FeignException.class));

		// Then
		org.junit.jupiter.api.Assertions.assertThrows(LoginProcessException.class, () -> {
			memberLoginUserDetailsService.loadUserByUsername("user");
		});

	}

}