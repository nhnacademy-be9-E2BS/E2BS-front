package com.nhnacademy.front.coupon.couponpolicy.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.ResponseCouponPolicyDTO;

@FeignClient(name = "coupon-policy-service", url = "${coupon.policy.url}")
public interface CouponPolicyAdaptor {

	/**
	 * 관리자 쿠폰 정책 등록
	 */
	@PostMapping
	ResponseEntity<Void> postCouponPolicy(@RequestBody RequestCouponPolicyDTO requestCouponPolicyDTO);

	/**
	 * 관리자 쿠폰 정책 전체 조회 
	 */
	@GetMapping
	ResponseEntity<PageResponse<ResponseCouponPolicyDTO>> getCouponPolicies(@SpringQueryMap Pageable pageable);

	/**
	 * 관리자 쿠폰 정책 단건 조회
	 */
	@GetMapping("/{couponPolicyId}")
	ResponseEntity<ResponseCouponPolicyDTO> getCouponPolicy(@PathVariable Long couponPolicyId);
}
