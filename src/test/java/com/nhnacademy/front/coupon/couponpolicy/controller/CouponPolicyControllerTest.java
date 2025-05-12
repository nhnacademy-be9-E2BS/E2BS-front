package com.nhnacademy.front.coupon.couponpolicy.controller;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.adaptor.CouponPolicyAdaptor;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(CouponPolicyController.class)
@ActiveProfiles("dev")
class CouponPolicyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CouponPolicyService couponPolicyService;

	@Test
	@DisplayName("쿠폰 정책 전체 조회 테스트")
	void testGetCouponPolicies() throws Exception {
		// Given
		ResponseCouponPolicyDTO policy = new ResponseCouponPolicyDTO();
		policy.setCouponPolicyName("Test Policy");
		PageResponse<ResponseCouponPolicyDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(policy));
		pageResponse.setTotalPages(10);
		pageResponse.setNumber(5);
		pageResponse.setSize(5);

		// When
		when(couponPolicyService.getCouponPolicies(5, 5)).thenReturn(pageResponse);

		// Then
		mockMvc.perform(get("/admin/mypage/couponPolicies?page=5&size=5"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/coupon/coupon-policy"))
			.andExpect(model().attributeExists("couponPolicies"))
			.andExpect(model().attribute("currentPage", 5))
			.andExpect(model().attribute("totalPages", 10))
			.andExpect(model().attribute("startPage", 3))
			.andExpect(model().attribute("endPage", 7));
	}

	@Test
	@DisplayName("쿠폰 정책 생성 테스트")
	void testCreateCouponPolicy() throws Exception {
		// Given
		RequestCouponPolicyDTO requestDTO = new RequestCouponPolicyDTO();
		requestDTO.setCouponPolicyName("New Policy");
		requestDTO.setCouponPolicyMinimum(1000);

		// When
		doNothing().when(couponPolicyService).createCouponPolicy(any(RequestCouponPolicyDTO.class));

		// Then
		mockMvc.perform(post("/admin/mypage/couponPolicies")
				.param("couponPolicyName", requestDTO.getCouponPolicyName())
				.param("couponPolicyMinimum", "1000")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/mypage/couponPolicies"));

		verify(couponPolicyService, times(1)).createCouponPolicy(any(RequestCouponPolicyDTO.class));
	}

	@Test
	@DisplayName("쿠폰 정책 단건 조회 테스트")
	void testGetCouponPolicyById() throws Exception {
		// Given
		Long id = 1L;
		ResponseCouponPolicyDTO policy = new ResponseCouponPolicyDTO();
		policy.setCouponPolicyName("Single Policy");

		// When
		when(couponPolicyService.getCouponPolicyById(id)).thenReturn(policy);

		// Then
		mockMvc.perform(get("/admin/mypage/couponPolicies/{couponPolicyId}", id))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/coupon/coupon-policy"))
			.andExpect(model().attributeExists("couponPolicies"))
			.andExpect(model().attribute("couponPolicies", policy))
			.andExpect(model().attribute("currentPage", 0))
			.andExpect(model().attribute("totalPages", 0))
			.andExpect(model().attribute("startPage", 0))
			.andExpect(model().attribute("endPage", 0));
	}
}
