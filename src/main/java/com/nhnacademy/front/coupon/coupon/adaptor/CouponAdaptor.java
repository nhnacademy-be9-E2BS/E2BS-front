package com.nhnacademy.front.coupon.coupon.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.coupon.model.dto.request.RequestCouponDTO;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;

@FeignClient(name = "coupon-service", url = "${coupon.url}")
public interface CouponAdaptor {

	@PostMapping
	ResponseEntity<Void> createCoupon(@RequestBody RequestCouponDTO request);

	@GetMapping
	ResponseEntity<PageResponse<ResponseCouponDTO>> getCoupons(Pageable pageable);

	@GetMapping("/{coupon-id}")
	ResponseEntity<ResponseCouponDTO> getCoupon(@PathVariable("coupon-id") Long couponId);

	@PutMapping("/{coupon-id}")
	ResponseEntity<Void> updateCoupon(@PathVariable("coupon-id") Long couponId);

	@GetMapping("/isActive")
	ResponseEntity<PageResponse<ResponseCouponDTO>> getCouponsIsActive(Pageable pageable);
}
