package com.nhnacademy.front.coupon.membercoupon.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.membercoupon.adaptor.MemberCouponAdaptor;
import com.nhnacademy.front.coupon.membercoupon.adaptor.MemberCouponBoxAdaptor;
import com.nhnacademy.front.coupon.membercoupon.exception.CouponBoxGetProcessException;
import com.nhnacademy.front.coupon.membercoupon.exception.IssueCouponToAllMemberProcessException;
import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.service.MemberCouponService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

	private final MemberCouponAdaptor memberCouponAdaptor;
	private final MemberCouponBoxAdaptor memberCouponBoxAdaptor;

	@Override
	public void issueCouponToAllMember(RequestAllMemberCouponDTO request) throws FeignException {
		ResponseEntity<Void> response = memberCouponAdaptor.postMemberCoupons(request);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new IssueCouponToAllMemberProcessException("전체 회원 쿠폰 발급 실패");
		}
	}

	@Override
	public PageResponse<ResponseMemberCouponDTO> getMemberCouponsByMemberId(String memberId, Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseMemberCouponDTO>> response = memberCouponBoxAdaptor.getMemberCouponsByMemberId(memberId, pageable);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponBoxGetProcessException("회원 아이디로 쿠폰 조회 실패");
		}
		return response.getBody();
	}
}
