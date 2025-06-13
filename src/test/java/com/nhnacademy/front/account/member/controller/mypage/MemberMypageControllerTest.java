package com.nhnacademy.front.account.member.controller.mypage;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.account.memberrank.model.domain.RankName;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import jakarta.servlet.http.HttpServletRequest;

@ActiveProfiles("dev")
@WithMockUser(username = "user", roles = {"MEMBER", "ADMIN", "USER"})
@WebMvcTest(controllers = MemberMypageController.class)
class MemberMypageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MemberService memberService;

	@MockitoBean
	private MemberMypageService memberMypageService;

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
	@DisplayName("마이페이지 화면 테스트")
	void getMypageMethodTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			// When
			when(memberMypageService.getMemberOrder("user")).thenReturn(1);
			when(memberService.getMemberName(any(HttpServletRequest.class))).thenReturn("user");
			when(memberMypageService.getMemberCoupon(new RequestMemberIdDTO("user"))).thenReturn(1);
			when(memberMypageService.getMemberPoint(new RequestMemberIdDTO("user"))).thenReturn(1L);
			when(memberMypageService.getMemberRankName(any(HttpServletRequest.class))).thenReturn(RankName.NORMAL);

			// Then
			mockMvc.perform(get("/mypage"))
				.andExpect(status().isOk())
				.andExpect(view().name("member/mypage/mypage"));
		}
	}

}