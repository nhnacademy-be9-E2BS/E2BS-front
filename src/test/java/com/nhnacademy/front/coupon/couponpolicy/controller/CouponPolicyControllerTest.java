package com.nhnacademy.front.coupon.couponpolicy.controller;

import com.nhnacademy.front.coupon.couponpolicy.model.dto.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponPolicyController.class)
class CouponPolicyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CouponPolicyService couponPolicyServiceImpl;

	@Test
	@DisplayName("쿠폰 정책 전체 조회 성공")
	void testGetCouponPolicies() throws Exception {
		ResponseCouponPolicyDTO dto = new ResponseCouponPolicyDTO();
		dto.setCouponPolicyName("TestPolicy");
		PageResponse<ResponseCouponPolicyDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(dto));
		pageResponse.setTotalPages(1);
		pageResponse.setSize(5);

		when(couponPolicyServiceImpl.getCouponPolicies(0, 5)).thenReturn(pageResponse);

		mockMvc.perform(get("/admin/couponPolicies?page=0&size=5"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/coupon/coupon-policy"))
			.andExpect(model().attributeExists("couponPolicies"))
			.andExpect(model().attribute("currentPage", 0))
			.andExpect(model().attribute("totalPages", 1));
	}

	@Test
	@DisplayName("쿠폰 정책 생성 성공")
	void testCreateCouponPolicy() throws Exception {
		RequestCouponPolicyDTO requestDTO = new RequestCouponPolicyDTO();
		requestDTO.setCouponPolicyName("NewPolicy");
		requestDTO.setCouponPolicyMinimum(1000);
		requestDTO.setCouponPolicyCreatedAt(LocalDateTime.now());

		mockMvc.perform(post("/admin/couponPolicies")
				.param("couponPolicyName", requestDTO.getCouponPolicyName())
				.param("couponPolicyMinimum", "1000")
				.param("couponPolicyCreatedAt", requestDTO.getCouponPolicyCreatedAt().toString())
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/couponPolicies"));

		verify(couponPolicyServiceImpl).createCouponPolicy(any(RequestCouponPolicyDTO.class));
	}

	@Test
	@DisplayName("쿠폰 정책 단건 조회")
	void testGetCouponPolicyById() throws Exception {
		Long id = 1L;
		ResponseCouponPolicyDTO responseDTO = new ResponseCouponPolicyDTO();
		responseDTO.setCouponPolicyName("SinglePolicy");

		when(couponPolicyServiceImpl.getCouponPolicyById(id)).thenReturn(responseDTO);

		mockMvc.perform(get("/admin/couponPolicies/{id}", id))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/coupon/coupon-policy"))
			.andExpect(model().attributeExists("couponPolicies"))
			.andExpect(model().attribute("currentPage", 1))
			.andExpect(model().attribute("totalPages", 1));
	}
}
