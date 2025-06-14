package com.nhnacademy.front.account.member.controller.dormant;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.account.member.model.dto.request.RequestDormantEmailNumberDTO;
import com.nhnacademy.front.account.member.service.MemberDormantService;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;

import jakarta.servlet.http.HttpServletRequest;

@ActiveProfiles("dev")
@WithMockUser(username = "user", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = MemberEmailDormantController.class)
class MemberEmailDormantControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MemberDormantService memberDormantService;

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
	@DisplayName("회원 휴면 Email 방식 화면 테스트")
	void getMemberDormantEmailMethodTest() throws Exception {

		// Given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("remainingCnt", 0);

		// When

		// Then
		mockMvc.perform(get("/member/user/dormant/email").session(session))
			.andExpect(status().isOk())
			.andExpect(view().name("member/dormant/dormant-email"));

	}

	@Test
	@DisplayName("회원 휴먼 Email 인증 번호 전송 메서드 테스트")
	void postEmailAuthenticationNumberMethodTest() throws Exception {

		// Given
		MockHttpSession session = new MockHttpSession();

		// When
		when(memberDormantService.createEmailAuthenticationNumber("user")).thenReturn("000000");
		when(memberDormantService.getMemberEmail("user")).thenReturn("user@naver.com");
		doNothing().when(memberDormantService).sendEmailAuthenticationNumber("user@naver.com", "000000");

		// Then
		mockMvc.perform(post("/member/user/dormant/email").session(session)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/member/dormant/email/user"));
	}

	@Test
	@DisplayName("회원 휴먼 Email 인증 번호 요청 메서드 테스트")
	void postEmailAuthenticationMethodTest() throws Exception {

		// Given
		RequestDormantEmailNumberDTO requestDormantEmailNumberDTO = new RequestDormantEmailNumberDTO(
			"000000"
		);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", "user");

		// When
		when(memberDormantService.checkEmailAuthenticationNumber(requestDormantEmailNumberDTO, "user")).thenReturn(
			true);
		doNothing().when(memberDormantService).changeMemberStateActive(eq("user"), any(HttpServletRequest.class));

		// Then
		mockMvc.perform(post("/member/dormant/email").session(session)
				.with(csrf())
				.param("dormantEmailNumber", "000000"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/members/login"));

	}

	@Test
	@DisplayName("회원 휴먼 Email 인증 번호 요청 메서드 ValidationFailedException 테스트")
	void postEmailAuthenticationMethodValidationFailedExceptionTest() throws Exception {

		// Given
		RequestDormantEmailNumberDTO requestDormantEmailNumberDTO = new RequestDormantEmailNumberDTO(
			"000000"
		);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", "user");

		// When
		when(memberDormantService.checkEmailAuthenticationNumber(requestDormantEmailNumberDTO, "user")).thenReturn(
			true);
		doNothing().when(memberDormantService).changeMemberStateActive(eq("user"), any(HttpServletRequest.class));

		// Then
		mockMvc.perform(post("/member/dormant/email").session(session)
				.with(csrf()))
			.andExpect(status().is4xxClientError());

	}

	@Test
	@DisplayName("회원 휴먼 Email 인증 번호 요청 메서드 DormantProcessingException 테스트")
	void postEmailAuthenticationMethodDormantProcessingExceptionExceptionTest() throws Exception {

		// Given
		RequestDormantEmailNumberDTO requestDormantEmailNumberDTO = new RequestDormantEmailNumberDTO(
			"000000"
		);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", null);

		// When
		when(memberDormantService.checkEmailAuthenticationNumber(requestDormantEmailNumberDTO, "user")).thenReturn(
			true);
		doNothing().when(memberDormantService).changeMemberStateActive(eq("user"), any(HttpServletRequest.class));

		// Then
		mockMvc.perform(post("/member/dormant/email").session(session)
				.with(csrf())
				.param("dormantEmailNumber", requestDormantEmailNumberDTO.getDormantEmailNumber()))
			.andExpect(status().is3xxRedirection());

	}

	@Test
	@DisplayName("회원 휴먼 Email 인증 번호 요청 메서드 DormantDoorayNotMatchedNumberException 테스트")
	void postEmailAuthenticationMethodDormantDoorayNotMatchedNumberExceptionTest() throws Exception {

		// Given
		RequestDormantEmailNumberDTO requestDormantEmailNumberDTO = new RequestDormantEmailNumberDTO(
			"000000"
		);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", "user");

		// When
		when(memberDormantService.checkEmailAuthenticationNumber(requestDormantEmailNumberDTO, "user")).thenReturn(
			false);
		doNothing().when(memberDormantService).changeMemberStateActive(eq("user"), any(HttpServletRequest.class));

		// Then
		mockMvc.perform(post("/member/dormant/email").session(session)
				.with(csrf())
				.param("dormantEmailNumber", "000000"))
			.andExpect(status().is4xxClientError());

	}

}