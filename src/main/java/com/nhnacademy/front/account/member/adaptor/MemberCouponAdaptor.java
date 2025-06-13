package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.member.model.dto.response.ResponseMypageMemberCouponDTO;

@FeignClient(name = "gateway-service", contextId = "member-coupon-adaptor")
public interface MemberCouponAdaptor {

	@GetMapping("/api/auth/mypage/{member-id}/coupons/counts")
	ResponseMypageMemberCouponDTO getMemberCouponAmount(@PathVariable("member-id") String memberId);

}
