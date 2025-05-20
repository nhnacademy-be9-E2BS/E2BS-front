package com.nhnacademy.front.coupon.couponpolicy.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.adaptor.CouponPolicyAdaptor;
import com.nhnacademy.front.coupon.couponpolicy.exception.CouponPolicyProcessException;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.request.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.response.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponPolicyServiceImpl implements CouponPolicyService {

	private final CouponPolicyAdaptor couponPolicyAdaptor;

	@Override
	public void createCouponPolicy(RequestCouponPolicyDTO requestCouponPolicyDTO) throws FeignException {
		ResponseEntity<Void> response = couponPolicyAdaptor.postCouponPolicy(requestCouponPolicyDTO);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponPolicyProcessException("쿠폰 등록 실패: " + response.getStatusCode());
		}
	}

	@Override
	public PageResponse<ResponseCouponPolicyDTO> getCouponPolicies(Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseCouponPolicyDTO>> response = couponPolicyAdaptor.getCouponPolicies(
			pageable);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponPolicyProcessException("전체 쿠폰 정책 조회 실패: " + response.getStatusCode());
		}
		return response.getBody();
	}

	@Override
	public ResponseCouponPolicyDTO getCouponPolicyById(Long couponPolicyId) throws FeignException {
		ResponseEntity<ResponseCouponPolicyDTO> response = couponPolicyAdaptor.getCouponPolicy(couponPolicyId);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponPolicyProcessException("단일 쿠폰 정책 조회 실패: " + response.getStatusCode());
		}
		return response.getBody();
	}

}
