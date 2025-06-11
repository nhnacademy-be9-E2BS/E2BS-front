package com.nhnacademy.front.account.oauth.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.account.oauth.model.dto.response.ResponsePaycoMemberInfoDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseProviderPaycoAccessTokenDTO;
import com.nhnacademy.front.account.oauth.service.OAuthService;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ActiveProfiles("dev")
@WithMockUser(username = "user", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = OAuthController.class)
class OAuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OAuthService oAuthService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	@DisplayName("PAYCO 로그인 화면 테스트")
	void getPaycoLoginTest() throws Exception {

		// Given

		// When

		// Then
		mockMvc.perform(get("/oauth2/authorization/payco"))
			.andExpect(status().is3xxRedirection());

	}

	@Test
	@DisplayName("PAYCO 로그인 요청 테스트")
	void paycoLoginTest() throws Exception {

		// Given
		ResponseProviderPaycoAccessTokenDTO responseProviderPaycoAccessTokenDTO = new ResponseProviderPaycoAccessTokenDTO();
		ResponsePaycoMemberInfoDTO responsePaycoMemberInfoDTO = new ResponsePaycoMemberInfoDTO();

		// When
		when(oAuthService.getPaycoAccessToken("user")).thenReturn(responseProviderPaycoAccessTokenDTO);
		when(oAuthService.getPaycoMemberInfo("access")).thenReturn(responsePaycoMemberInfoDTO);
		doNothing().when(oAuthService).paycoLogin(responsePaycoMemberInfoDTO, request, response);

		// Then
		mockMvc.perform(get("/login/oauth2/code/payco?code=user"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login?error"));

	}

}