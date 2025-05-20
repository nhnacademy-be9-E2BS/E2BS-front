package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberCouponDTO;

@FeignClient(name = "member-coupon-adaptor", url = "${member.mypage.member.url}")
public interface MemberCouponAdaptor {

	@GetMapping("/{memberId}/coupons")
	ResponseMemberCouponDTO getMemberCouponAmount(@PathVariable("memberId") String memberId);

}
