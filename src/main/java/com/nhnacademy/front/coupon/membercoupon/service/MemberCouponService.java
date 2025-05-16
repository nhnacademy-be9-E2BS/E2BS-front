package com.nhnacademy.front.coupon.membercoupon.service;

import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;

public interface MemberCouponService {

	void issueCouponToAllMember(RequestAllMemberCouponDTO request);

}
