package com.nhnacademy.front.coupon.couponpolicy.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.request.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.response.ResponseCouponPolicyDTO;

@FeignClient(name = "gateway-service", contextId = "coupon-policy-service")
public interface CouponPolicyAdaptor {

	/**
	 * 관리자 쿠폰 정책 등록
	 */
	@PostMapping("/api/admin/couponPolicies")
	ResponseEntity<Void> postCouponPolicy(@RequestBody RequestCouponPolicyDTO requestCouponPolicyDTO);

	/**
	 * 관리자 쿠폰 정책 전체 조회 
	 */
	@GetMapping("/api/admin/couponPolicies")
	ResponseEntity<PageResponse<ResponseCouponPolicyDTO>> getCouponPolicies(Pageable pageable);

	/**
	 * 관리자 쿠폰 정책 단건 조회
	 */
	@GetMapping("/api/admin/couponPolicies/{coupon-policy-id}")
	ResponseEntity<ResponseCouponPolicyDTO> getCouponPolicy(@PathVariable("coupon-policy-id") Long couponPolicyId);
}
