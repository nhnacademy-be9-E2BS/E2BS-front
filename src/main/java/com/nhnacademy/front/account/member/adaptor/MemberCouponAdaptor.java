package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.member.model.dto.response.ResponseMypageMemberCouponDTO;

@FeignClient(name = "member-coupon-adaptor", url = "${auth.member.mypage.url}")
public interface MemberCouponAdaptor {

	@GetMapping("/{memberId}/coupons/counts")
	ResponseMypageMemberCouponDTO getMemberCouponAmount(@PathVariable("memberId") String memberId);

}
