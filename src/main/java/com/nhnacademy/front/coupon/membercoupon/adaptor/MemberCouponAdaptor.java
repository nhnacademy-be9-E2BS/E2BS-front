package com.nhnacademy.front.coupon.membercoupon.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;

@FeignClient(name = "member-coupon-service", url = "${coupon.member.url}")
public interface MemberCouponAdaptor {

	@PostMapping("/issue")
	ResponseEntity<Void> postMemberCoupons(@RequestBody RequestAllMemberCouponDTO request);

}
