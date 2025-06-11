package com.nhnacademy.front.coupon.membercoupon.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseOrderCouponDTO;

@FeignClient(name = "member-coupon-order-service", url = "${coupon.order.url}")
public interface MemberCouponOrderAdaptor {

	@GetMapping("/{member-id}/coupons")
	ResponseEntity<List<ResponseOrderCouponDTO>> getCouponsInOrder(@PathVariable("member-id") String memberId, @RequestParam List<Long> request);
}
