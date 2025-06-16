package com.nhnacademy.front.account.member.controller.mypage;

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

import com.nhnacademy.front.account.customer.model.domain.Customer;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.account.memberrank.model.domain.MemberRank;
import com.nhnacademy.front.account.memberrank.model.domain.RankName;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRole;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRoleName;
import com.nhnacademy.front.account.memberstate.model.domain.MemberState;
import com.nhnacademy.front.account.memberstate.model.domain.MemberStateName;
import com.nhnacademy.front.account.socialauth.model.domain.SocialAuth;
import com.nhnacademy.front.account.socialauth.model.domain.SocialAuthName;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ActiveProfiles("dev")
@WithMockUser(username = "user", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = MemberInfoMypageController.class)
class MemberInfoMypageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MemberMypageService memberMypageService;

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
	@DisplayName("마이페이지 개인정보 화면 테스트")
	void getMemberInfoMethodTest() throws Exception {

		// Given
		Customer customer = new Customer(1L, "user@naver.com", "1234", "1234");

		MemberRank memberRank = new MemberRank(1L, RankName.NORMAL, 1, 1L);

		MemberState memberState = new MemberState(1, MemberStateName.ACTIVE);

		MemberRole memberRole = new MemberRole(1, MemberRoleName.MEMBER);

		SocialAuth socialAuth = new SocialAuth(1L, SocialAuthName.WEB);

		ResponseMemberInfoDTO responseMemberInfoDTO = new ResponseMemberInfoDTO(
			customer,
			"user",
			LocalDate.now(),
			"01012345678",
			LocalDate.now(),
			LocalDate.now(),
			memberRank,
			memberState,
			memberRole,
			socialAuth
		);

		// When
		when(memberMypageService.getMemberInfo(any(HttpServletRequest.class))).thenReturn(responseMemberInfoDTO);

		// Then
		mockMvc.perform(get("/mypage/info"))
			.andExpect(status().isOk())
			.andExpect(view().name("member/mypage/memberinfo/member-info"));

	}

	@Test
	@DisplayName("마이페이지 개인정보 요청 메서드 테스트")
	void updateMemberInfoMethodTest() throws Exception {

		// Given
		RequestMemberInfoDTO requestMemberInfoDTO = new RequestMemberInfoDTO(
			"user",
			"김도윤",
			"user@naver.com",
			LocalDate.now(),
			"01012345678",
			"1234",
			"1234"
		);

		// When
		doNothing().when(memberMypageService)
			.updateMemberInfo(any(HttpServletRequest.class), any(RequestMemberInfoDTO.class));

		// Then
		mockMvc.perform(put("/mypage/info")
				.with(csrf())
				.param("memberId", requestMemberInfoDTO.getMemberId())
				.param("customerName", requestMemberInfoDTO.getCustomerName())
				.param("customerEmail", requestMemberInfoDTO.getCustomerEmail())
				.param("memberBirth", "2000-01-01")
				.param("memberPhone", requestMemberInfoDTO.getMemberPhone())
				.param("customerPassword", requestMemberInfoDTO.getCustomerPassword())
				.param("customerPasswordCheck", requestMemberInfoDTO.getCustomerPasswordCheck()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/mypage/info"));

	}

	@Test
	@DisplayName("마이페이지 개인정보 요청 메서드 ValidationFailedException 테스트")
	void updateMemberInfoMethodValidationFailedExceptionTest() throws Exception {

		// Given
		RequestMemberInfoDTO requestMemberInfoDTO = new RequestMemberInfoDTO(
			"user",
			"김도윤",
			"user@naver.com",
			LocalDate.now(),
			"01012345678",
			"1234",
			"1234"
		);

		// When
		doNothing().when(memberMypageService)
			.updateMemberInfo(any(HttpServletRequest.class), any(RequestMemberInfoDTO.class));

		// Then
		mockMvc.perform(put("/mypage/info")
				.with(csrf())
				.param("memberId", requestMemberInfoDTO.getMemberId())
				.param("customerName", requestMemberInfoDTO.getCustomerName())
				.param("customerEmail", requestMemberInfoDTO.getCustomerEmail())
				.param("memberPhone", requestMemberInfoDTO.getMemberPhone())
				.param("customerPassword", requestMemberInfoDTO.getCustomerPassword())
				.param("customerPasswordCheck", requestMemberInfoDTO.getCustomerPasswordCheck()))
			.andExpect(status().is3xxRedirection());

	}

	@Test
	@DisplayName("회원 탈퇴 메서드 테스트")
	void withdrawMemberMethodTest() throws Exception {

		// Given

		// When
		doNothing().when(memberMypageService)
			.withdrawMember(any(HttpServletRequest.class), any(HttpServletResponse.class));

		// Then
		mockMvc.perform(post("/mypage/info")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"));

	}

}