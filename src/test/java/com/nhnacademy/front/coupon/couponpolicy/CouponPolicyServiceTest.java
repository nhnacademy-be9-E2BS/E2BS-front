package com.nhnacademy.front.coupon.couponpolicy;

import com.nhnacademy.front.common.exception.EmptyRequestException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.adaptor.CouponPolicyAdaptor;
import com.nhnacademy.front.coupon.couponpolicy.exception.CouponPolicyProcessException;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.request.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.response.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.impl.CouponPolicyServiceImpl;

import feign.FeignException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
	@DisplayName("쿠폰 정책 생성 실패 - Null DTO")
	void testCreateCouponPolicy_NullRequest() {
		// when & then
		assertThatThrownBy(() -> couponPolicyServiceImpl.createCouponPolicy(null))
			.isInstanceOf(EmptyRequestException.class)
			.hasMessageContaining("요청 값을 받지 못했습니다.");
	}

	@Test
	@DisplayName("쿠폰 정책 생성 실패 - Feign 예외")
	void testCreateCouponPolicyFeignException() {
		// given
		RequestCouponPolicyDTO dto = new RequestCouponPolicyDTO();
		dto.setCouponPolicyName("Test");

		when(couponPolicyAdaptor.postCouponPolicy(dto)).thenThrow(mock(FeignException.class));

		// when
		assertThatThrownBy(() -> couponPolicyServiceImpl.createCouponPolicy(dto))
			.isInstanceOf(CouponPolicyProcessException.class)
			.hasMessageContaining("쿠폰 등록 실패");
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

	@Test
	@DisplayName("쿠폰 정책 단건 조회 실패 - FeignException")
	void testGetCouponPolicyByIdFeignException() {
		// given
		when(couponPolicyAdaptor.getCouponPolicy(1L)).thenThrow(mock(FeignException.class));

		// when & then
		assertThatThrownBy(() -> couponPolicyServiceImpl.getCouponPolicyById(1L))
			.isInstanceOf(CouponPolicyProcessException.class)
			.hasMessageContaining("쿠폰 정책 조회 실패");
	}
}
