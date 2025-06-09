package com.nhnacademy.front.coupon.coupon.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.coupon.adaptor.CouponAdaptor;
import com.nhnacademy.front.coupon.coupon.exception.CouponCreateProcessException;
import com.nhnacademy.front.coupon.coupon.exception.CouponGetProcessException;
import com.nhnacademy.front.coupon.coupon.exception.CouponUpdateProcessException;
import com.nhnacademy.front.coupon.coupon.model.dto.request.RequestCouponDTO;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;
import com.nhnacademy.front.coupon.coupon.service.CouponService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

	private final CouponAdaptor couponAdaptor;

	@Override
	public void createCoupon(RequestCouponDTO request) throws FeignException {
		ResponseEntity<Void> response = couponAdaptor.createCoupon(request);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponCreateProcessException("쿠폰 등록 실패");
		}
	}

	@Override
	public PageResponse<ResponseCouponDTO> getCoupons(Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseCouponDTO>> response = couponAdaptor.getCoupons(pageable);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponGetProcessException("쿠폰 리스트 조회 실패");
		}
		return response.getBody();
	}

	@Override
	public ResponseCouponDTO getCoupon(Long couponId) throws FeignException {
		ResponseEntity<ResponseCouponDTO> response = couponAdaptor.getCoupon(couponId);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponGetProcessException("쿠폰 단건 조회 실패");
		}
		return response.getBody();
	}

	@Override
	public void updateCoupon(Long couponId) throws FeignException{
		ResponseEntity<Void> response = couponAdaptor.updateCoupon(couponId);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponUpdateProcessException("쿠폰 상태 업데이트 실패");
		}
	}

	@Override
	public PageResponse<ResponseCouponDTO> getCouponsIsActive(Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseCouponDTO>> response = couponAdaptor.getCouponsIsActive(pageable);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponGetProcessException("쿠폰 리스트 조회 실패");
		}
		return response.getBody();
	}
}
