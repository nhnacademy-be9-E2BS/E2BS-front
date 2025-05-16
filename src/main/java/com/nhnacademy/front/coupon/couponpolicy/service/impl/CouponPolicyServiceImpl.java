package com.nhnacademy.front.coupon.couponpolicy.service.impl;

import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.exception.EmptyRequestException;
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
	public void createCouponPolicy(RequestCouponPolicyDTO requestCouponPolicyDTO) {
		if (Objects.isNull(requestCouponPolicyDTO)) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
		}

		try {
			ResponseEntity<Void> response = couponPolicyAdaptor.postCouponPolicy(requestCouponPolicyDTO);
			if(!response.getStatusCode().is2xxSuccessful()) {
				throw new CouponPolicyProcessException("쿠폰 등록 실패: " + response.getStatusCode());
			}
		} catch (FeignException ex) {
			throw new CouponPolicyProcessException("쿠폰 등록 실패: " + ex.getMessage());
		}
	}

	@Override
	public PageResponse<ResponseCouponPolicyDTO> getCouponPolicies(Pageable pageable) {
		try {
			ResponseEntity<PageResponse<ResponseCouponPolicyDTO>> response = couponPolicyAdaptor.getCouponPolicies(pageable);

			if(!response.getStatusCode().is2xxSuccessful()) {
				throw new CouponPolicyProcessException("쿠폰 정책 조회 실패: " + response.getStatusCode());
			}
			return response.getBody();
		} catch (FeignException ex) {
			throw new CouponPolicyProcessException("쿠폰 정책 조회 실패: " + ex.getMessage());
		}
	}

	@Override
	public ResponseCouponPolicyDTO getCouponPolicyById(Long couponPolicyId) {
		try {
			ResponseEntity<ResponseCouponPolicyDTO> response = couponPolicyAdaptor.getCouponPolicy(couponPolicyId);
			if(!response.getStatusCode().is2xxSuccessful()) {
				throw new CouponPolicyProcessException("쿠폰 정책 조회 실패: " + response.getStatusCode());
			}
			return response.getBody();
		} catch (FeignException ex) {
			throw new CouponPolicyProcessException("쿠폰 정책 조회 실패: " + ex.getMessage());
		}
	}

}
