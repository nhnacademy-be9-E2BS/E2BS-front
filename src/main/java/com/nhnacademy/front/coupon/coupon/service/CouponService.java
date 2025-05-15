package com.nhnacademy.front.coupon.coupon.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.coupon.model.dto.request.RequestCouponDTO;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;

public interface CouponService {
	/**
	 * 관리자 쿠폰 생성
	 */
	void createCoupon(RequestCouponDTO request);

	/**
	 * 관리자 쿠폰 전체 조회
	 */
	PageResponse<ResponseCouponDTO> getCoupons(Pageable pageable);

	/**
	 * 관리자 쿠폰 ID로 단건 조회
	 */
	ResponseCouponDTO getCoupon(Long couponId);

	/**
	 * 쿠폰 활성 상태 변경
	 */
	void updateCoupon(Long couponId);
}
