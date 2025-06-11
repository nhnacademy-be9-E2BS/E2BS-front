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

import com.nhnacademy.front.account.member.model.dto.request.RequestDoorayAuthenticationDTO;
import com.nhnacademy.front.account.member.model.dto.request.RequestDormantDoorayNumberDTO;
import com.nhnacademy.front.account.member.service.MemberDormantService;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;

import jakarta.servlet.http.HttpServletRequest;

@ActiveProfiles("dev")
@WithMockUser(username = "user", roles = {"MEMBER", "ADMIN", "USER"})
@WebMvcTest(controllers = MemberDoorayDormantController.class)
class MemberDoorayDormantControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MemberDormantService memberDormantService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	@DisplayName("휴면 회원 휴면 처리 방법 선택 메서드 테스트")
	void getMemberDormantMethodTest() throws Exception {

		// Given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", "user");

		// When

		// Then
		mockMvc.perform(get("/member/login/dormant").session(session))
			.andExpect(status().isOk())
			.andExpect(view().name("member/dormant/dormant-method"));

	}

	@Test
	@DisplayName("휴면 회원 휴면 처리 방법 선택 메서드 DormantProcessingException 테스트")
	void getMemberDormantMethodDormantProcessingExceptionTest() throws Exception {

		// Given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", null);

		// When

		// Then
		mockMvc.perform(get("/member/login/dormant").session(session))
			.andExpect(status().is3xxRedirection());

	}

	@Test
	@DisplayName("휴면 회원 Dooray 처리 메서드 테스트")
	void getMemberDormantDoorayMethodTest() throws Exception {

		// Given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("remainingCnt", 1);

		// When

		// Then
		mockMvc.perform(get("/member/dormant/dooray/user"))
			.andExpect(status().isOk())
			.andExpect(view().name("member/dormant/dormant-dooray"));

	}

	@Test
	@DisplayName("휴면 회원 Dooray 처리 인증 번호 전송 메서드 테스트")
	void postDoorayAuthenticationNumberMethodTest() throws Exception {

		// Given

		// When
		when(memberDormantService.createDoorayAuthenticationNumber("user")).thenReturn("000000");
		doNothing().when(memberDormantService).sendDoorayMessageAuthenticationNumber(
			new RequestDoorayAuthenticationDTO("E2BS 관리자", "000000")
		);

		// Then
		mockMvc.perform(post("/member/dormant/dooray/user")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/member/dormant/dooray/user"));

	}

	@Test
	@DisplayName("휴면 회원 휴면 상태 변경 메서드 테스트")
	void postDoorayAuthenticationMethodTest() throws Exception {

		// Given
		RequestDormantDoorayNumberDTO requestDormantDoorayNumberDTO = new RequestDormantDoorayNumberDTO(
			"000000"
		);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", "user");

		// When
		when(memberDormantService.checkDoorayAuthenticationNumber(requestDormantDoorayNumberDTO, "user")).thenReturn(
			true);
		doNothing().when(memberDormantService).changeMemberStateActive(eq("user"), any(HttpServletRequest.class));

		// Then
		mockMvc.perform(post("/member/dormant/dooray").session(session)
				.with(csrf())
				.param("dormantDoorayNumber", requestDormantDoorayNumberDTO.getDormantDoorayNumber()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/members/login"));

	}

	@Test
	@DisplayName("휴면 회원 휴면 상태 변경 메서드 ValidationFailedException 테스트")
	void postDoorayAuthenticationMethodValidationFailedExceptionTest() throws Exception {

		// Given
		RequestDormantDoorayNumberDTO requestDormantDoorayNumberDTO = new RequestDormantDoorayNumberDTO(
			"000000"
		);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", "user");

		// When
		when(memberDormantService.checkDoorayAuthenticationNumber(requestDormantDoorayNumberDTO, "user")).thenReturn(
			true);
		doNothing().when(memberDormantService).changeMemberStateActive(eq("user"), any(HttpServletRequest.class));

		// Then
		mockMvc.perform(post("/member/dormant/dooray").session(session)
				.with(csrf()))
			.andExpect(status().is4xxClientError());

	}

	@Test
	@DisplayName("휴면 회원 휴면 상태 변경 메서드 DormantProcessingException 테스트")
	void postDoorayAuthenticationMethodDormantProcessingExceptionTest() throws Exception {

		// Given
		RequestDormantDoorayNumberDTO requestDormantDoorayNumberDTO = new RequestDormantDoorayNumberDTO(
			"000000"
		);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", null);

		// When
		when(memberDormantService.checkDoorayAuthenticationNumber(requestDormantDoorayNumberDTO, "user")).thenReturn(
			true);
		doNothing().when(memberDormantService).changeMemberStateActive(eq("user"), any(HttpServletRequest.class));

		// Then
		mockMvc.perform(post("/member/dormant/dooray").session(session)
				.with(csrf())
				.param("dormantDoorayNumber", requestDormantDoorayNumberDTO.getDormantDoorayNumber()))
			.andExpect(status().is3xxRedirection());

	}

	@Test
	@DisplayName("휴면 회원 휴면 상태 변경 메서드 DormantDoorayNotMatchedNumberException 테스트")
	void postDoorayAuthenticationMethodDormantDoorayNotMatchedNumberExceptionTest() throws Exception {

		// Given
		RequestDormantDoorayNumberDTO requestDormantDoorayNumberDTO = new RequestDormantDoorayNumberDTO(
			"000000"
		);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("dormantMemberId", "user");

		// When
		when(memberDormantService.checkDoorayAuthenticationNumber(requestDormantDoorayNumberDTO, "user")).thenReturn(
			false);
		doNothing().when(memberDormantService).changeMemberStateActive(eq("user"), any(HttpServletRequest.class));

		// Then
		mockMvc.perform(post("/member/dormant/dooray").session(session)
				.with(csrf())
				.param("dormantDoorayNumber", requestDormantDoorayNumberDTO.getDormantDoorayNumber()))
			.andExpect(status().is4xxClientError());

	}

}