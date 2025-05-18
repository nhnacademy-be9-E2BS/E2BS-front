package com.nhnacademy.front.coupon.couponpolicy.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.ResponseCouponPolicyDTO;

public interface CouponPolicyService {

	void createCouponPolicy(RequestCouponPolicyDTO requestCouponPolicyDTO);

	PageResponse<ResponseCouponPolicyDTO> getCouponPolicies(Pageable pageable);

	ResponseCouponPolicyDTO getCouponPolicyById(Long couponPolicyId);
}
