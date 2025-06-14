package com.nhnacademy.front.pointpolicy;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.pointpolicy.controller.PointPolicyController;
import com.nhnacademy.front.pointpolicy.model.domain.PointPolicyType;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyRegisterDTO;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyUpdateDTO;
import com.nhnacademy.front.pointpolicy.model.dto.response.ResponsePointPolicyDTO;
import com.nhnacademy.front.pointpolicy.service.PointPolicyService;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(PointPolicyController.class)
@ActiveProfiles("dev")
class PointPolicyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PointPolicyService pointPolicyService;

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
	@DisplayName("포인트 정책 전체 조회 - 정상 호출")
	void testGetPointPolicies() throws Exception {
		List<ResponsePointPolicyDTO> emptyList = List.of();

		when(pointPolicyService.getRegisterPointPolicy()).thenReturn(emptyList);
		when(pointPolicyService.getReviewImgPointPolicy()).thenReturn(emptyList);
		when(pointPolicyService.getReviewPointPolicy()).thenReturn(emptyList);
		when(pointPolicyService.getBookPointPolicy()).thenReturn(emptyList);

		mockMvc.perform(get("/admin/settings/pointPolicies"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/pointpolicy/point-policy"))
			.andExpect(model().attributeExists("policySections"));

		verify(pointPolicyService).getRegisterPointPolicy();
		verify(pointPolicyService).getReviewImgPointPolicy();
		verify(pointPolicyService).getReviewPointPolicy();
		verify(pointPolicyService).getBookPointPolicy();
	}

	@Test
	@DisplayName("포인트 정책 등록 폼 페이지 - 정상 호출")
	void testGetPointPoliciesRegisterForm() throws Exception {
		mockMvc.perform(get("/admin/settings/pointPolicies/register"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/pointpolicy/point-policy-register"))
			.andExpect(model().attribute("pointPolicyTypes", PointPolicyType.values()));
	}

	@Test
	@DisplayName("포인트 정책 등록 처리 - 성공")
	void testCreatePointPoliciesSuccess() throws Exception {
		mockMvc.perform(post("/admin/settings/pointPolicies/register")
				.param("pointPolicyName", "Test Policy")
				.param("pointPolicyFigure", "10")
				.param("pointPolicyType", "REGISTER")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/pointPolicies"));

		verify(pointPolicyService).createPointPolicy(any(RequestPointPolicyRegisterDTO.class));
	}

	@Test
	@DisplayName("포인트 정책 등록 처리 - 유효성 검증 실패 시 400 에러 발생")
	void testCreatePointPoliciesValidationFail() throws Exception {
		mockMvc.perform(post("/admin/settings/pointPolicies/register")
				.param("policyName", "")
				.param("policyValue", "10")
				.param("policyType", "REGISTER")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.with(csrf()))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("포인트 정책 활성화/비활성화 - 성공")
	void testActivatePointPolicy() throws Exception {
		mockMvc.perform(put("/admin/settings/pointPolicies/1/activate")
				.with(csrf()))
			.andExpect(status().isOk());

		verify(pointPolicyService).activatePointPolicy(1L);
	}

	@Test
	@DisplayName("포인트 정책 수치 수정 - 성공")
	void testUpdatePointPolicySuccess() throws Exception {
		mockMvc.perform(put("/admin/settings/pointPolicies/1")
				.param("pointPolicyFigure", "20")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.with(csrf()))
			.andExpect(status().isOk());

		verify(pointPolicyService).updatePointPolicy(eq(1L), any(RequestPointPolicyUpdateDTO.class));
	}

	@Test
	@DisplayName("포인트 정책 수치 수정 - 유효성 실패")
	void testUpdatePointPolicyValidationFail() throws Exception {
		mockMvc.perform(put("/admin/settings/pointPolicies/1")
				.param("pointPolicyFigure", "aaa")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.with(csrf()))
			.andExpect(status().isBadRequest());
	}
}
