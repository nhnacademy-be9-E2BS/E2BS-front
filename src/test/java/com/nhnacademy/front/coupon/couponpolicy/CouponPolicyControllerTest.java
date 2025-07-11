package com.nhnacademy.front.coupon.couponpolicy;

import static org.mockito.ArgumentMatchers.*;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.controller.CouponPolicyController;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.request.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.response.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(CouponPolicyController.class)
@ActiveProfiles("dev")
class CouponPolicyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CouponPolicyService couponPolicyService;

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
	@DisplayName("쿠폰 정책 전체 조회")
	void testGetCouponPolicies() throws Exception {
		ResponseCouponPolicyDTO dto = new ResponseCouponPolicyDTO();
		List<ResponseCouponPolicyDTO> content = List.of(dto);
		PageResponse<ResponseCouponPolicyDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(content);
		pageResponse.setSize(5);

		when(couponPolicyService.getCouponPolicies(any(Pageable.class))).thenReturn(pageResponse);

		mockMvc.perform(get("/admin/settings/couponPolicies"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/coupon/coupon-policy"))
			.andExpect(model().attributeExists("couponPolicies"));
	}

	@Test
	@DisplayName("쿠폰 정책 단건 조회")
	void testGetCouponPolicyById() throws Exception {
		ResponseCouponPolicyDTO dto = new ResponseCouponPolicyDTO();

		when((couponPolicyService.getCouponPolicyById(1L))).thenReturn(dto);

		mockMvc.perform(get("/admin/settings/couponPolicies/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/coupon/coupon-policy-detail"))
			.andExpect(model().attributeExists("couponPolicy"));
	}

	@Test
	@DisplayName("쿠폰 정책 등록")
	void testCreateCouponPolicy() throws Exception {
		doNothing().when(couponPolicyService).createCouponPolicy(any(RequestCouponPolicyDTO.class));

		mockMvc.perform(post("/admin/settings/couponPolicies")
				.param("couponPolicyName", "NewPolicy")
				.param("couponPolicyMinimum", "1000")
				.param("couponPolicyDiscountRate", "10")
				.param("couponPolicySalePrice", "500")
				.param("couponPolicyMaximumAmount", "10000")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/couponPolicies"));
	}
}
