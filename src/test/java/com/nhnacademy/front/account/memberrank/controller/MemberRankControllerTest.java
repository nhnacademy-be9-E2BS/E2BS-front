package com.nhnacademy.front.account.memberrank.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.account.memberrank.model.domain.RankName;
import com.nhnacademy.front.account.memberrank.model.dto.response.ResponseMemberRankDTO;
import com.nhnacademy.front.account.memberrank.service.MemberRankService;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRole;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRoleName;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.home.model.dto.response.ResponseHomeMemberNameDTO;
import com.nhnacademy.front.home.service.HomeService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import jakarta.servlet.http.HttpServletRequest;

@ActiveProfiles("dev")
@WithMockUser(username = "user", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = MemberRankController.class)
class MemberRankControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MemberService memberService;

	@MockitoBean
	private MemberMypageService memberMypageService;

	@MockitoBean
	private MemberRankService memberRankService;

	@MockitoBean
	private HomeService homeService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;

	@Mock
	private HttpServletRequest request;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	@DisplayName("마이페이지 회원 등급 조회 테스트")
	void getMemberRankMethodTest() throws Exception {

		// Given
		try (MockedStatic<JwtGetMemberId> jwtStatic = mockStatic(JwtGetMemberId.class)) {
			jwtStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn("user");

			ResponseHomeMemberNameDTO responseHomeMemberNameDTO = new ResponseHomeMemberNameDTO(
				"user", "user", new MemberRole(1, MemberRoleName.MEMBER)
			);

			List<ResponseMemberRankDTO> memberRanks = List.of(
				new ResponseMemberRankDTO(RankName.NORMAL, 5, 1L),
				new ResponseMemberRankDTO(RankName.GOLD, 10, 2L)
			);

			// When
			when(homeService.getMemberNameFromHome(any())).thenReturn(responseHomeMemberNameDTO);
			when(memberService.getMemberName(any())).thenReturn("user");
			when(memberMypageService.getMemberRankName(any())).thenReturn(RankName.NORMAL);
			when(memberRankService.getMemberRank("user")).thenReturn(memberRanks);

			// Then
			mockMvc.perform(get("/mypage/rank"))
				.andExpect(status().isOk())
				.andExpect(view().name("member/mypage/memberrank/mypage-rank"));
		}
	}

}