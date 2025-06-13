package com.nhnacademy.front.coupon.coupon;

import static org.assertj.core.api.Assertions.*;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.coupon.controller.CouponController;
import com.nhnacademy.front.coupon.coupon.model.dto.request.RequestCouponDTO;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;
import com.nhnacademy.front.coupon.coupon.service.CouponService;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.response.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;
import com.nhnacademy.front.elasticsearch.service.ProductSearchService;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductAdminService;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = CouponController.class)
@ActiveProfiles("dev")
class CouponControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private CouponService couponService;
	@MockitoBean
	private CouponPolicyService couponPolicyService;
	@MockitoBean
	private AdminCategoryService adminCategoryService;
	@MockitoBean
	private ProductAdminService productAdminService;
	@MockitoBean
	private ProductSearchService productSearchService;
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
	@DisplayName("쿠폰 등록 폼 페이지 조회")
	void testCreateForm() throws Exception {
		// 서비스 응답 설정
		PageResponse<ResponseCouponPolicyDTO> policyPage = new PageResponse<>();
		policyPage.setContent(List.of(new ResponseCouponPolicyDTO()));
		policyPage.setSize(10);
		when(couponPolicyService.getCouponPolicies(any())).thenReturn(policyPage);

		when(adminCategoryService.getCategories()).thenReturn(List.of(new ResponseCategoryDTO()));

		PageResponse<ResponseProductReadDTO> productPage = new PageResponse<>();
		productPage.setContent(List.of(new ResponseProductReadDTO()));
		productPage.setSize(10);
		when(productAdminService.getProducts(any())).thenReturn(productPage);

		// 테스트 실행
		mockMvc.perform(get("/admin/settings/coupons/register"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/coupon/coupon-register"))
			.andExpect(model().attributeExists("couponType", "categories", "products", "couponPolicies"));

	}

	@Test
	@DisplayName("쿠폰 등록 - 성공")
	void testCreateCouponSuccess() throws Exception {
		doNothing().when(couponService).createCoupon(any(RequestCouponDTO.class));

		mockMvc.perform(post("/admin/settings/coupons/register")
				.param("couponPolicyId", "1")
				.param("couponName", "TestCoupon")
				.param("productId", "1")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/coupons"));
	}

	@Test
	@DisplayName("쿠폰 등록 - 유효성 실패")
	void testCreateCouponFail() throws Exception {
		// couponTitle 누락하면 ValidationFailedException 발생
		mockMvc.perform(post("/admin/settings/coupons/register")
				.param("couponPolicyId", "1")
				.param("couponType", "product")
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("쿠폰 전체 조회")
	void testGetCoupons() throws Exception {
		PageResponse<ResponseCouponDTO> page = new PageResponse<>();
		page.setContent(List.of(new ResponseCouponDTO()));
		page.setSize(10);
		when(couponService.getCoupons(any())).thenReturn(page);

		mockMvc.perform(get("/admin/settings/coupons"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/coupon/coupon"))
			.andExpect(model().attributeExists("coupons"));
	}

	@Test
	@DisplayName("쿠폰 단건 조회")
	void testGetCouponById() throws Exception {
		when(couponService.getCoupon(1L)).thenReturn(new ResponseCouponDTO());

		mockMvc.perform(get("/admin/settings/coupons/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/coupon/coupon-detail"))
			.andExpect(model().attributeExists("coupon"));
	}

	@Test
	@DisplayName("쿠폰 활성 상태 변경")
	void testUpdateCoupon() throws Exception {
		doNothing().when(couponService).updateCoupon(1L);

		mockMvc.perform(put("/admin/settings/coupons/1")
				.with(csrf()))
			.andExpect(status().isOk());
	}
}
