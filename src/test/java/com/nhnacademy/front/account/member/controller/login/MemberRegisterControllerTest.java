package com.nhnacademy.front.account.member.controller.login;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.cart.service.MemberCartService;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;

@ActiveProfiles("dev")
@WithMockUser(username = "user", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = MemberRegisterController.class)
class MemberRegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MemberService memberService;

	@MockitoBean
	private MemberCartService cartService;

	@MockitoBean
	private CartInterceptor cartInterceptor;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;

	@BeforeEach
	void setUp() throws Exception {
		when(cartInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	@DisplayName("회원가입 화면 테스트")
	void getRegisterTest() throws Exception {

		// Given

		// When

		// Then
		mockMvc.perform(get("/members/register"))
			.andExpect(status().isOk())
			.andExpect(view().name("member/login/register"));

	}

	@Test
	@DisplayName("회원가입 요청 처리 테스트")
	void createRegisterTest() throws Exception {

		// Given
		RequestRegisterMemberDTO requestRegisterMemberDTO = new RequestRegisterMemberDTO(
			"user",
			"김도윤",
			"1234",
			"1234",
			"user@naver.com",
			LocalDate.now(),
			"01012345678"
		);

		// When
		doNothing().when(memberService).createMember(requestRegisterMemberDTO);
		doNothing().when(cartService).createCartByMember("user");

		// Then
		mockMvc.perform(post("/members/register")
				.with(csrf())
				.param("memberId", requestRegisterMemberDTO.getMemberId())
				.param("customerName", requestRegisterMemberDTO.getCustomerName())
				.param("customerPassword", requestRegisterMemberDTO.getCustomerPassword())
				.param("customerPasswordCheck", requestRegisterMemberDTO.getCustomerPasswordCheck())
				.param("customerEmail", requestRegisterMemberDTO.getCustomerEmail())
				.param("memberBirth", String.valueOf(requestRegisterMemberDTO.getMemberBirth()))
				.param("memberPhone", requestRegisterMemberDTO.getMemberPhone()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/members/login"));

	}

	@Test
	@DisplayName("회원가입 요청 처리 ValidationFailedException 테스트")
	void createRegisterValidationFailedExceptionTest() throws Exception {

		// Given
		RequestRegisterMemberDTO requestRegisterMemberDTO = new RequestRegisterMemberDTO(
			"user",
			"김도윤",
			"1234",
			"1234",
			"user@naver.com",
			LocalDate.now(),
			"01012345678"
		);

		// When
		doNothing().when(memberService).createMember(requestRegisterMemberDTO);

		// Then
		mockMvc.perform(post("/members/register")
				.with(csrf())
				.param("memberId", requestRegisterMemberDTO.getMemberId())
				.param("customerName", requestRegisterMemberDTO.getCustomerName())
				.param("customerPassword", requestRegisterMemberDTO.getCustomerPassword())
				.param("customerPasswordCheck", requestRegisterMemberDTO.getCustomerPasswordCheck())
				.param("customerEmail", requestRegisterMemberDTO.getCustomerEmail())
				.param("memberBirth", String.valueOf(requestRegisterMemberDTO.getMemberBirth())))
			.andExpect(status().is4xxClientError());

	}

}