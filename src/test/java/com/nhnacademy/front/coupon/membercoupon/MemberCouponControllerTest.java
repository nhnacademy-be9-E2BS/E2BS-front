package com.nhnacademy.front.coupon.membercoupon;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
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
import com.nhnacademy.front.common.interceptor.CartInterceptor;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;
import com.nhnacademy.front.coupon.coupon.service.CouponService;
import com.nhnacademy.front.coupon.membercoupon.controller.MemberCouponController;
import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.service.MemberCouponService;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = MemberCouponController.class)
@ActiveProfiles("dev")
class MemberCouponControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private CouponService couponService;

	@MockitoBean
	private MemberCouponService memberCouponService;

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
	@DisplayName("회원 쿠폰 발급 폼 페이지")
	void testGetMemberCouponsForm() throws Exception {
		PageResponse<ResponseCouponDTO> page = new PageResponse<>();
		page.setContent(List.of(new ResponseCouponDTO()));
		page.setSize(5);

		when(couponService.getCouponsIsActive(any())).thenReturn(page);

		mockMvc.perform(get("/admin/settings/memberCoupons/issue"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/coupon/coupon-issue"))
			.andExpect(model().attributeExists("couponsIsActive"));
	}

	@Test
	@DisplayName("전체 회원 쿠폰 발급 성공")
	void testPostMemberCouponsSuccess() throws Exception {
		doNothing().when(memberCouponService).issueCouponToAllMember(any(RequestAllMemberCouponDTO.class));

		mockMvc.perform(post("/admin/settings/memberCoupons/issue")
				.param("couponId", "1")
				.param("memberCouponPeriod", String.valueOf(LocalDateTime.now()))
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/memberCoupons/issue"));

		verify(memberCouponService, times(1)).issueCouponToAllMember(any());
	}

	@Test
	@DisplayName("전체 회원 쿠폰 발급 실패 (유효성 검증)")
	void testPostMemberCouponsFailValidation() throws Exception {
		mockMvc.perform(post("/admin/settings/memberCoupons/issue")
				.param("couponId", "")  // 누락/잘못된 값
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result ->
				assertThat(result.getResolvedException())
					.isInstanceOf(ValidationFailedException.class)
			);
	}

	@Test
	@DisplayName("마이페이지 쿠폰함 - 전체")
	void testGetMemberCouponBox_All() throws Exception {
		LocalDateTime now = LocalDateTime.now();

		ResponseMemberCouponDTO couponDTO = new ResponseMemberCouponDTO(
			1L, "테스트쿠폰", "10% 할인",
			null,
			null,
			null,
			null,
			now.minusDays(5),
			now.plusDays(5),
			false
		);

		PageResponse<ResponseMemberCouponDTO> page = new PageResponse<>();
		page.setContent(List.of(couponDTO));
		page.setSize(10);

		when(memberCouponService.getUsableMemberCouponsByMemberId(any(), any())).thenReturn(page);
		when(memberCouponService.getMemberCouponsByMemberId(any(), any())).thenReturn(page);

		mockMvc.perform(get("/mypage/couponBox")
				.param("status", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("member/mypage/coupon-box"))
			.andExpect(model().attributeExists("memberCoupons", "usableCouponCount", "status"));
	}


	@Test
	@DisplayName("마이페이지 쿠폰함 - 사용 가능")
	void testGetMemberCouponBox_Usable() throws Exception {
		LocalDateTime now = LocalDateTime.now();

		ResponseMemberCouponDTO couponDTO = new ResponseMemberCouponDTO(
			1L, "테스트쿠폰", "10% 할인",
			null,
			null,
			null,
			null,
			now.minusDays(5),
			now.plusDays(5),
			false
		);

		PageResponse<ResponseMemberCouponDTO> page = new PageResponse<>();
		page.setContent(List.of(couponDTO));
		page.setSize(10);

		when(memberCouponService.getUsableMemberCouponsByMemberId(any(), any())).thenReturn(page);

		mockMvc.perform(get("/mypage/couponBox")
				.param("status", "2"))
			.andExpect(status().isOk())
			.andExpect(view().name("member/mypage/coupon-box"))
			.andExpect(model().attributeExists("memberCoupons", "usableCouponCount", "status"));
	}
}
