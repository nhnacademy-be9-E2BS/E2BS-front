package com.nhnacademy.front.coupon.membercoupon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseOrderCouponDTO;

public interface MemberCouponService {

	void issueCouponToAllMember(RequestAllMemberCouponDTO request);

	PageResponse<ResponseMemberCouponDTO> getMemberCouponsByMemberId(String memberId, Pageable pageable);

	PageResponse<ResponseMemberCouponDTO> getUsableMemberCouponsByMemberId(String memberId, Pageable pageable);

	PageResponse<ResponseMemberCouponDTO> getUnusableMemberCouponsByMemberId(String memberId, Pageable pageable);

	/**
	 * 주문서에서 적용 가능한 쿠폰 리스트 조회
	 */
	List<ResponseOrderCouponDTO> getCouponsInOrder(String memberId, List<Long> request);
}
