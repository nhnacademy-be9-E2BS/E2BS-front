package com.nhnacademy.front.coupon.coupon.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.coupon.adaptor.CouponAdaptor;
import com.nhnacademy.front.coupon.coupon.exception.CouponCreateProcessException;
import com.nhnacademy.front.coupon.coupon.exception.CouponGetProcessException;
import com.nhnacademy.front.coupon.coupon.model.dto.request.RequestCouponDTO;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;
import com.nhnacademy.front.coupon.coupon.exception.CouponUpdateProcessException;
import com.nhnacademy.front.coupon.coupon.service.CouponService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

	private final CouponAdaptor couponAdaptor;

	@Override
	public void createCoupon(RequestCouponDTO request) {
		try {
			ResponseEntity<Void> response = couponAdaptor.createCoupon(request);
			if(!response.getStatusCode().is2xxSuccessful()) {
				throw new CouponCreateProcessException("쿠폰 등록 실패");
			}
		} catch (FeignException ex) {
			throw new CouponCreateProcessException("쿠폰 등록 실패");
		}
	}

	@Override
	public PageResponse<ResponseCouponDTO> getCoupons(Pageable pageable) {
		try {
			ResponseEntity<PageResponse<ResponseCouponDTO>> response = couponAdaptor.getCoupons(pageable);
			if(!response.getStatusCode().is2xxSuccessful()) {
				throw new CouponGetProcessException("쿠폰 리스트 조회 실패");
			}
			return response.getBody();
		} catch (FeignException ex) {
			throw new CouponGetProcessException("쿠폰 리스트 조회 실패");
		}
	}

	@Override
	public ResponseCouponDTO getCoupon(Long couponId) {
		try {
			ResponseEntity<ResponseCouponDTO> response = couponAdaptor.getCoupon(couponId);
			if(!response.getStatusCode().is2xxSuccessful()) {
				throw new CouponGetProcessException("쿠폰 단건 조회 실패");
			}
			return response.getBody();
		} catch (FeignException ex) {
			throw new CouponGetProcessException("쿠폰 단건 조회 실패");
		}
	}

	@Override
	public void updateCoupon(Long couponId) {
		try {
			couponAdaptor.updateCoupon(couponId);
		} catch (FeignException ex) {
			throw new CouponUpdateProcessException("쿠폰 활성 상태 변경 실패");
		}
	}
}
