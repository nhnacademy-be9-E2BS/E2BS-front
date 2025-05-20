package com.nhnacademy.front.coupon.couponpolicy;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.adaptor.CouponPolicyAdaptor;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.request.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.response.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.impl.CouponPolicyServiceImpl;

@ExtendWith(MockitoExtension.class)
class CouponPolicyServiceTest {

	@Mock
	private CouponPolicyAdaptor couponPolicyAdaptor;

	@InjectMocks
	private CouponPolicyServiceImpl couponPolicyServiceImpl;

	@Test
	@DisplayName("쿠폰 정책 생성 성공")
	void testCreateCouponPolicySuccess() {
		// given
		RequestCouponPolicyDTO dto = new RequestCouponPolicyDTO();
		dto.setCouponPolicyName("Test");
		dto.setCouponPolicyMinimum(1000);

		when(couponPolicyAdaptor.postCouponPolicy(dto)).thenReturn(ResponseEntity.ok().build());

		// when
		couponPolicyServiceImpl.createCouponPolicy(dto);

		// then
		verify(couponPolicyAdaptor).postCouponPolicy(any(RequestCouponPolicyDTO.class));
	}

	@Test
	@DisplayName("쿠폰 정책 전체 조회 성공")
	void testGetCouponPoliciesSuccess() {
		// given
		PageResponse<ResponseCouponPolicyDTO> page = new PageResponse<>();
		page.setContent(List.of(new ResponseCouponPolicyDTO()));
		page.setTotalPages(1);
		page.setNumber(0);
		page.setSize(10);
		Pageable pageable = PageRequest.of(0, 10);

		when(couponPolicyAdaptor.getCouponPolicies(pageable)).thenReturn(ResponseEntity.ok(page));

		// when
		PageResponse<ResponseCouponPolicyDTO> result = couponPolicyServiceImpl.getCouponPolicies(pageable);

		// then
		assertThat(result.getContent()).hasSize(1);
		verify(couponPolicyAdaptor).getCouponPolicies(pageable);
	}

	@Test
	@DisplayName("쿠폰 정책 단건 조회 성공")
	void testGetCouponPolicyByIdSuccess() {
		// given
		ResponseCouponPolicyDTO dto = new ResponseCouponPolicyDTO();
		dto.setCouponPolicyName("Sample");

		when(couponPolicyAdaptor.getCouponPolicy(1L)).thenReturn(ResponseEntity.ok(dto));

		// when
		ResponseCouponPolicyDTO result = couponPolicyServiceImpl.getCouponPolicyById(1L);

		// then
		assertThat(result.getCouponPolicyName()).isEqualTo("Sample");
		verify(couponPolicyAdaptor).getCouponPolicy(1L);
	}
}
