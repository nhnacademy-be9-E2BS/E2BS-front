package com.nhnacademy.front.coupon.couponpolicy.service;

import com.nhnacademy.front.coupon.couponpolicy.model.dto.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.ResponseCouponPolicyDTO;

public interface CouponPolicyService {

	void createCouponPolicy(RequestCouponPolicyDTO requestCouponPolicyDTO);

	PageResponse<ResponseCouponPolicyDTO> getCouponPolicies(int page, int size);

	ResponseCouponPolicyDTO getCouponPolicyById(Long couponPolicyId);
}
