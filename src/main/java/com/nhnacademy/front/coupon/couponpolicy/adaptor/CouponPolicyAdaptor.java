package com.nhnacademy.front.coupon.couponpolicy.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.coupon.couponpolicy.model.dto.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.ResponseCouponPolicyDTO;

@FeignClient(name = "coupon-policy-service", url = "${coupon.policy.url}")
public interface CouponPolicyAdaptor {

	/**
	 * 관리자 쿠폰 정책 등록
	 */
	@PostMapping
	void postCouponPolicy(@RequestBody RequestCouponPolicyDTO requestCouponPolicyDTO);

	/**
	 * 관리자 쿠폰 정책 전체 조회 
	 */
	@GetMapping
	PageResponse<ResponseCouponPolicyDTO> getCouponPolicies(@RequestParam("page") int page,
															@RequestParam("size") int size);

	/**
	 * 관리자 쿠폰 정책 단건 조회
	 */
	@GetMapping("/{couponPolicyId}")
	ResponseCouponPolicyDTO getCouponPolicy(@PathVariable Long couponPolicyId);
}
