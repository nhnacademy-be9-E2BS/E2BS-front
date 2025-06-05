package com.nhnacademy.front.coupon.membercoupon.service.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.membercoupon.adaptor.MemberCouponAdaptor;
import com.nhnacademy.front.coupon.membercoupon.adaptor.MemberCouponBoxAdaptor;
import com.nhnacademy.front.coupon.membercoupon.adaptor.MemberCouponOrderAdaptor;
import com.nhnacademy.front.coupon.membercoupon.exception.CouponBoxGetProcessException;
import com.nhnacademy.front.coupon.membercoupon.exception.IssueCouponToAllMemberProcessException;
import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseOrderCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.service.MemberCouponService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

	private final MemberCouponAdaptor memberCouponAdaptor;
	private final MemberCouponBoxAdaptor memberCouponBoxAdaptor;
	private final MemberCouponOrderAdaptor memberCouponOrderAdaptor;

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
			throw new CouponBoxGetProcessException("회원 아이디로 전체 쿠폰 조회 실패");
		}
		return response.getBody();
	}

	@Override
	public PageResponse<ResponseMemberCouponDTO> getUsableMemberCouponsByMemberId(String memberId, Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseMemberCouponDTO>> response = memberCouponBoxAdaptor.getUsableMemberCouponsByMemberId(memberId, pageable);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponBoxGetProcessException("회원 아이디로 사용가능 쿠폰 조회 실패");
		}
		return response.getBody();
	}

	@Override
	public PageResponse<ResponseMemberCouponDTO> getUnusableMemberCouponsByMemberId(String memberId, Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseMemberCouponDTO>> response = memberCouponBoxAdaptor.getUnusableMemberCouponsByMemberId(memberId, pageable);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponBoxGetProcessException("회원 아이디로 사용불가 쿠폰 조회 실패");
		}
		return response.getBody();
	}

	@Override
	public List<ResponseOrderCouponDTO> getCouponsInOrder(String memberId, List<Long> request) {
		ResponseEntity<List<ResponseOrderCouponDTO>> response = memberCouponOrderAdaptor.getCouponsInOrder(memberId, request);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new CouponBoxGetProcessException("회원 아이디로 주문서에서 적용 가능한 쿠폰 조회 실패");
		}
		return response.getBody();
	}
}
