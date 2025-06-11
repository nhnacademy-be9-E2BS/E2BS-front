package com.nhnacademy.front.coupon.coupon;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.coupon.adaptor.CouponAdaptor;
import com.nhnacademy.front.coupon.coupon.exception.CouponCreateProcessException;
import com.nhnacademy.front.coupon.coupon.exception.CouponGetProcessException;
import com.nhnacademy.front.coupon.coupon.exception.CouponUpdateProcessException;
import com.nhnacademy.front.coupon.coupon.model.dto.request.RequestCouponDTO;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;
import com.nhnacademy.front.coupon.coupon.service.impl.CouponServiceImpl;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

	@Mock
	private CouponAdaptor couponAdaptor;

	@InjectMocks
	private CouponServiceImpl couponService;

	@Test
	@DisplayName("쿠폰 생성 성공")
	void testCreateCouponSuccess() {
		RequestCouponDTO dto = new RequestCouponDTO();

		when(couponAdaptor.createCoupon(dto)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

		couponService.createCoupon(dto);

		verify(couponAdaptor).createCoupon(dto);
	}

	@Test
	@DisplayName("쿠폰 생성 실패")
	void testCreateCouponFail() {
		RequestCouponDTO dto = new RequestCouponDTO();

		when(couponAdaptor.createCoupon(dto)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		assertThatThrownBy(() -> couponService.createCoupon(dto))
			.isInstanceOf(CouponCreateProcessException.class)
			.hasMessageContaining("쿠폰 등록 실패");

		verify(couponAdaptor).createCoupon(dto);
	}

	@Test
	@DisplayName("쿠폰 전체 조회 성공")
	void testGetCouponsSuccess() {
		Pageable pageable = PageRequest.of(0, 10);
		PageResponse<ResponseCouponDTO> page = new PageResponse<>();
		page.setContent(List.of(new ResponseCouponDTO()));

		when(couponAdaptor.getCoupons(pageable)).thenReturn(ResponseEntity.ok(page));

		PageResponse<ResponseCouponDTO> result = couponService.getCoupons(pageable);

		assertThat(result.getContent()).hasSize(1);
		verify(couponAdaptor).getCoupons(pageable);
	}

	@Test
	@DisplayName("쿠폰 전체 조회 실패")
	void testGetCouponsFail() {
		Pageable pageable = PageRequest.of(0, 10);

		when(couponAdaptor.getCoupons(pageable)).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

		assertThatThrownBy(() -> couponService.getCoupons(pageable))
			.isInstanceOf(CouponGetProcessException.class)
			.hasMessageContaining("쿠폰 리스트 조회 실패");

		verify(couponAdaptor).getCoupons(pageable);
	}

	@Test
	@DisplayName("쿠폰 단건 조회 성공")
	void testGetCouponSuccess() {
		ResponseCouponDTO dto = new ResponseCouponDTO();
		dto.setCouponName("Spring 할인");

		when(couponAdaptor.getCoupon(1L)).thenReturn(ResponseEntity.ok(dto));

		ResponseCouponDTO result = couponService.getCoupon(1L);

		assertThat(result.getCouponName()).isEqualTo("Spring 할인");
		verify(couponAdaptor).getCoupon(1L);
	}

	@Test
	@DisplayName("쿠폰 단건 조회 실패")
	void testGetCouponFail() {
		when(couponAdaptor.getCoupon(1L)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

		assertThatThrownBy(() -> couponService.getCoupon(1L))
			.isInstanceOf(CouponGetProcessException.class)
			.hasMessageContaining("쿠폰 단건 조회 실패");

		verify(couponAdaptor).getCoupon(1L);
	}

	@Test
	@DisplayName("쿠폰 업데이트 성공")
	void testUpdateCouponSuccess() {
		when(couponAdaptor.updateCoupon(1L)).thenReturn(ResponseEntity.ok().build());

		couponService.updateCoupon(1L);

		verify(couponAdaptor).updateCoupon(1L);
	}

	@Test
	@DisplayName("쿠폰 업데이트 실패")
	void testUpdateCouponFail() {
		when(couponAdaptor.updateCoupon(1L)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		assertThatThrownBy(() -> couponService.updateCoupon(1L))
			.isInstanceOf(CouponUpdateProcessException.class)
			.hasMessageContaining("쿠폰 상태 업데이트 실패");

		verify(couponAdaptor).updateCoupon(1L);
	}

	@Test
	@DisplayName("활성 쿠폰 조회 성공")
	void testGetCouponsIsActiveSuccess() {
		Pageable pageable = PageRequest.of(0, 10);
		PageResponse<ResponseCouponDTO> page = new PageResponse<>();
		page.setContent(List.of(new ResponseCouponDTO()));

		when(couponAdaptor.getCouponsIsActive(pageable)).thenReturn(ResponseEntity.ok(page));

		PageResponse<ResponseCouponDTO> result = couponService.getCouponsIsActive(pageable);

		assertThat(result.getContent()).hasSize(1);
		verify(couponAdaptor).getCouponsIsActive(pageable);
	}

	@Test
	@DisplayName("활성 쿠폰 조회 실패")
	void testGetCouponsIsActiveFail() {
		Pageable pageable = PageRequest.of(0, 10);

		when(couponAdaptor.getCouponsIsActive(pageable)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		assertThatThrownBy(() -> couponService.getCouponsIsActive(pageable))
			.isInstanceOf(CouponGetProcessException.class)
			.hasMessageContaining("쿠폰 리스트 조회 실패");

		verify(couponAdaptor).getCouponsIsActive(pageable);
	}
}
