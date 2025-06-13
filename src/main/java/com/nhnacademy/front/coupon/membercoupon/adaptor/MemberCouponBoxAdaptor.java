package com.nhnacademy.front.coupon.membercoupon.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;

@FeignClient(name = "gateway-service", contextId = "member-coupon-box")
public interface MemberCouponBoxAdaptor {

	@GetMapping("/api/auth/mypage/{member-id}/coupons")
	ResponseEntity<PageResponse<ResponseMemberCouponDTO>> getMemberCouponsByMemberId(@PathVariable("member-id") String memberId,
		Pageable pageable);

	@GetMapping("/api/auth/mypage/{member-id}/couponsUsable")
	ResponseEntity<PageResponse<ResponseMemberCouponDTO>> getUsableMemberCouponsByMemberId(@PathVariable("member-id") String memberId,
		Pageable pageable);

	@GetMapping("/api/auth/mypage/{member-id}/couponsUnusable")
	ResponseEntity<PageResponse<ResponseMemberCouponDTO>> getUnusableMemberCouponsByMemberId(@PathVariable("member-id") String memberId,
		Pageable pageable);
}
