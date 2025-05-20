package com.nhnacademy.front.coupon.membercoupon.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;

@FeignClient(name = "member-coupon-box", url = "${coupon.box.url}")
public interface MemberCouponBoxAdaptor {

	@GetMapping
	ResponseEntity<PageResponse<ResponseMemberCouponDTO>> getMemberCouponsByMemberId(@RequestParam Long memberId, @SpringQueryMap Pageable pageable);
}
