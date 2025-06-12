package com.nhnacademy.front.account.pointhistory;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRole;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRoleName;
import com.nhnacademy.front.account.pointhistory.controller.PointHistoryController;
import com.nhnacademy.front.account.pointhistory.model.dto.response.ResponsePointHistoryDTO;
import com.nhnacademy.front.account.pointhistory.service.PointHistoryService;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.home.model.dto.response.ResponseHomeMemberNameDTO;
import com.nhnacademy.front.home.service.HomeService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import jakarta.servlet.http.HttpServletRequest;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = PointHistoryController.class)
@ActiveProfiles("dev")
class PointHistoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private PointHistoryService pointHistoryService;

	@MockitoBean
	private MemberMypageService memberMypageService;

	@MockitoBean
	private HomeService homeService;

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
	@DisplayName("마이페이지 포인트 내역 조회 - 성공")
	void testGetPointHistorySuccess() throws Exception {
		String memberId = "member123";

		PageResponse<ResponsePointHistoryDTO> response = new PageResponse<>();
		response.setContent(List.of(new ResponsePointHistoryDTO()));
		response.setSize(10);

		when(homeService.getMemberNameFromHome(any(HttpServletRequest.class)))
			.thenReturn(new ResponseHomeMemberNameDTO(memberId, "USER", new MemberRole(1L, MemberRoleName.MEMBER)));

		when(pointHistoryService.getPointHistoryByMemberId(eq(memberId), any()))
			.thenReturn(response);

		when(memberMypageService.getMemberPoint(new RequestMemberIdDTO(memberId)))
			.thenReturn(1000L);

		try (MockedStatic<JwtGetMemberId> jwtMock = mockStatic(JwtGetMemberId.class)) {
			jwtMock.when(() -> JwtGetMemberId.jwtGetMemberId(any())).thenReturn(memberId);

			mockMvc.perform(get("/mypage/pointHistory"))
				.andExpect(status().isOk())
				.andExpect(view().name("member/mypage/point-history"))
				.andExpect(model().attributeExists("memberName"))
				.andExpect(model().attributeExists("memberRole"))
				.andExpect(model().attributeExists("pointHistories"))
				.andExpect(model().attributeExists("usablePoint"));
		}
	}
}
