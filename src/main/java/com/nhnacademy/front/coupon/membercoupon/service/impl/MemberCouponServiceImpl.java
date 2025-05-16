package com.nhnacademy.front.coupon.membercoupon.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.coupon.membercoupon.adaptor.MemberCouponAdaptor;
import com.nhnacademy.front.coupon.membercoupon.exception.IssueCouponToAllMemberProcessException;
import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.service.MemberCouponService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

	private final MemberCouponAdaptor memberCouponAdaptor;

	@Override
	public void issueCouponToAllMember(RequestAllMemberCouponDTO request) {
		try {
			ResponseEntity<Void> response = memberCouponAdaptor.postMemberCoupons(request);
			if(!response.getStatusCode().is2xxSuccessful()) {
				throw new IssueCouponToAllMemberProcessException("전체 회원 쿠폰 발급 실패");
			}
		} catch (FeignException ex){
			throw new IssueCouponToAllMemberProcessException("전체 회원 쿠폰 발급 실패");
		}
	}

}
