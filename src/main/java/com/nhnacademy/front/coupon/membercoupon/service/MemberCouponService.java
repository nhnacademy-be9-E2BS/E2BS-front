package com.nhnacademy.front.coupon.membercoupon.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;

public interface MemberCouponService {

	void issueCouponToAllMember(RequestAllMemberCouponDTO request);

	PageResponse<ResponseMemberCouponDTO> getMemberCouponsByMemberId(Long memberId, Pageable pageable);
}
