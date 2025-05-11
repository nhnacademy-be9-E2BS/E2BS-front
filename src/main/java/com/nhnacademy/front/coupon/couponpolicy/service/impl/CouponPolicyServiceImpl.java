package com.nhnacademy.front.coupon.couponpolicy.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.exception.EmptyRequestException;
import com.nhnacademy.front.coupon.couponpolicy.adaptor.CouponPolicyAdaptor;
import com.nhnacademy.front.coupon.couponpolicy.exception.CouponPolicyProcessException;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponPolicyServiceImpl implements CouponPolicyService {

	private final CouponPolicyAdaptor couponPolicyAdaptor;

	@Override
	public void createCouponPolicy(RequestCouponPolicyDTO requestCouponPolicyDTO) {
		if (Objects.isNull(requestCouponPolicyDTO)) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
		}

		try {
			couponPolicyAdaptor.postCouponPolicy(requestCouponPolicyDTO);
		} catch (FeignException ex) {
			throw new CouponPolicyProcessException("쿠폰 등록 실패: " + ex.getMessage());
		}
	}

	@Override
	public PageResponse<ResponseCouponPolicyDTO> getCouponPolicies(int page, int size) {
		try {
			return couponPolicyAdaptor.getCouponPolicies(page, size);
		} catch (FeignException ex) {
			throw new CouponPolicyProcessException("쿠폰 정책 조회 실패: " + ex.getMessage());
		}
	}

	@Override
	public ResponseCouponPolicyDTO getCouponPolicyById(Long couponPolicyId) {
		return couponPolicyAdaptor.getCouponPolicy(couponPolicyId);
	}
}
