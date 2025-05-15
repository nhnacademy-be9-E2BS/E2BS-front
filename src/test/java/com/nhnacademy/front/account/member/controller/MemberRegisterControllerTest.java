/*
package com.nhnacademy.front.account.member.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.common.exception.ValidationFailedException;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class MemberRegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MemberService memberService;

	@Test
	@DisplayName("회원가입 페이지 접근 가능")
	void registerPageAccessibleTest() throws Exception {

		// Given

		// When

		// Then
		mockMvc.perform(get("/register"))
			.andExpect(status().isOk());

	}

	@Test
	@DisplayName("회원가입 성공")
	void successCreateRegisterTest() throws Exception {

		// Given
		String memberId = "nhn1";
		String customerName = "HNHACADEMY";
		String customerPassword = "1234";
		String customerPasswordCheck = "1234";
		String customerEmail = "dooray@gmail.com";
		LocalDate memberBirth = LocalDate.now();
		String memberPhone = "01012345678";

		// When
		doNothing().when(memberService).createMember(any(RequestRegisterMemberDTO.class));

		// Then
		mockMvc.perform(post("/register")
				.param("memberId", memberId)
				.param("customerName", customerName)
				.param("customerPassword", customerPassword)
				.param("customerPasswordCheck", customerPasswordCheck)
				.param("customerEmail", customerEmail)
				.param("memberBirth", String.valueOf(memberBirth))
				.param("memberPhone", memberPhone))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"));

	}

	@Test
	@DisplayName("회원가입 ValidationFailedException 발생")
	void failRegisterValidationFailedExceptionTest() throws Exception {

		// Given
		String memberId = null;
		String customerName = "HNHACADEMY";
		String customerPassword = "1234";
		String customerPasswordCheck = "1234";
		String customerEmail = "dooray@gmail.com";
		Date memberBirth = null;
		String memberPhone = "01012345678";

		// When

		// Then
		mockMvc.perform(post("/register")
				.param("memberId", memberId)
				.param("customerPassword", customerPassword)
				.param("customerPasswordCheck", customerPasswordCheck)
				.param("customerEmail", customerEmail)
				.param("memberBirth", String.valueOf(memberBirth))
				.param("memberPhone", memberPhone))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));

	}

}

 */