package com.nhnacademy.front.coupon.membercoupon.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;

@FeignClient(name = "member-coupon-box", url = "${auth.member.mypage.url}")
public interface MemberCouponBoxAdaptor {

	@GetMapping("/{memberId}/coupons")
	ResponseEntity<PageResponse<ResponseMemberCouponDTO>> getMemberCouponsByMemberId(@PathVariable String memberId,
		Pageable pageable);
}
