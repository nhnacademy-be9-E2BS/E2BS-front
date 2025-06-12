package com.nhnacademy.front.account.member.model.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nhnacademy.front.account.memberrole.model.domain.MemberRoleName;

class LoginProcessMemberTest {

	@Test
	@DisplayName("LoginProcessMember 메서드 테스트")
	void getAuthoritiesMethodTest() throws Exception {

		// Given
		LoginProcessMember loginProcessMember = new LoginProcessMember(
			"user", "1234", MemberRoleName.MEMBER
		);

		// When

		// Then
		Assertions.assertThatCode(loginProcessMember::getAuthorities).doesNotThrowAnyException();

	}

	@Test
	@DisplayName("LoginProcessMember getPassword 메서드 테스트")
	void getPasswordMethodTest() throws Exception {

		// Given
		LoginProcessMember loginProcessMember = new LoginProcessMember(
			"user", "1234", MemberRoleName.MEMBER
		);

		// When

		// Then
		Assertions.assertThat(loginProcessMember.getPassword()).isEqualTo("1234");

	}

	@Test
	@DisplayName("LoginProcessMember getUsername 메서드 테스트")
	void getUsernameMethodTest() throws Exception {

		// Given
		LoginProcessMember loginProcessMember = new LoginProcessMember(
			"user", "1234", MemberRoleName.MEMBER
		);

		// When

		// Then
		Assertions.assertThat(loginProcessMember.getUsername()).isEqualTo("user");

	}

}