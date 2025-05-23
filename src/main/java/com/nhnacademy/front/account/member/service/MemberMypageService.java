package com.nhnacademy.front.account.member.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.member.adaptor.MemberCouponAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberInfoAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberPointHistoryAdaptor;
import com.nhnacademy.front.account.member.exception.NotFoundMemberRankNameException;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberPointDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMypageMemberCouponDTO;
import com.nhnacademy.front.account.memberrank.model.domain.RankName;
import com.nhnacademy.front.common.exception.EmptyResponseException;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberMypageService {

	private final MemberCouponAdaptor memberCouponAdaptor;
	private final MemberPointHistoryAdaptor memberPointHistoryAdaptor;
	private final MemberInfoAdaptor memberInfoAdaptor;

	public int getMemberCoupon(RequestMemberIdDTO requestMemberIdDTO) throws FeignException {
		ResponseMypageMemberCouponDTO responseMemberCouponDTO = memberCouponAdaptor
			.getMemberCouponAmount(requestMemberIdDTO.getMemberId());
		if (Objects.isNull(responseMemberCouponDTO)) {
			throw new EmptyResponseException("쿠폰 개수를 가져오지 못했습니다.");
		}

		return responseMemberCouponDTO.getCouponCnt();
	}

	public long getMemberPoint(RequestMemberIdDTO requestMemberIdDTO) throws FeignException {
		ResponseMemberPointDTO responseMemberPointDTO = memberPointHistoryAdaptor
			.getMemberPointAmount(requestMemberIdDTO.getMemberId());
		if (Objects.isNull(responseMemberPointDTO)) {
			throw new EmptyResponseException("포인트 정보를 가져오지 못했습니다.");
		}

		return responseMemberPointDTO.getPointAmount();
	}

	/**
	 * 회원의 등급을 가져오는 메서드
	 */
	public RankName getMemberRankName(HttpServletRequest request) throws FeignException {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		ResponseMemberInfoDTO memberInfoDTO = memberInfoAdaptor.getMemberInfo(memberId);
		if (Objects.isNull(memberInfoDTO)) {
			throw new NotFoundMemberRankNameException("회원의 등급 정보를 가져오지 못했습니다.");
		}

		return memberInfoDTO.getMemberRank().getMemberRankName();
	}

}
