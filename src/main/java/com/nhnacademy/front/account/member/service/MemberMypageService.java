package com.nhnacademy.front.account.member.service;

import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.member.adaptor.MemberCouponAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberInfoAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberPointHistoryAdaptor;
import com.nhnacademy.front.account.member.exception.NotFoundMemberInfoException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberRankNameException;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberPointDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMypageMemberCouponDTO;
import com.nhnacademy.front.account.memberrank.model.domain.RankName;
import com.nhnacademy.front.common.error.exception.EmptyResponseException;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.jwt.rule.JwtRule;

import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberMypageService {

	private final MemberCouponAdaptor memberCouponAdaptor;
	private final MemberPointHistoryAdaptor memberPointHistoryAdaptor;
	private final MemberInfoAdaptor memberInfoAdaptor;
	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 회원이 가지고 있는 쿠폰 개수를 가져오는 메서드
	 */
	public int getMemberCoupon(RequestMemberIdDTO requestMemberIdDTO) throws FeignException {
		ResponseMypageMemberCouponDTO responseMemberCouponDTO = memberCouponAdaptor
			.getMemberCouponAmount(requestMemberIdDTO.getMemberId());
		if (Objects.isNull(responseMemberCouponDTO)) {
			throw new EmptyResponseException();
		}

		return responseMemberCouponDTO.getCouponCnt();
	}

	/**
	 * 회원 전체 포인트를 가져오는 메서드
	 */
	public long getMemberPoint(RequestMemberIdDTO requestMemberIdDTO) throws FeignException {
		ResponseMemberPointDTO responseMemberPointDTO = memberPointHistoryAdaptor
			.getMemberPointAmount(requestMemberIdDTO.getMemberId());
		if (Objects.isNull(responseMemberPointDTO)) {
			throw new EmptyResponseException();
		}

		return responseMemberPointDTO.getPointAmount();
	}

	/**
	 * 회원의 등급을 가져오는 메서드
	 */
	public RankName getMemberRankName(HttpServletRequest request) throws FeignException {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		ResponseEntity<ResponseMemberInfoDTO> memberInfoDTO = memberInfoAdaptor.getMemberInfo(memberId);
		if (!memberInfoDTO.getStatusCode().is2xxSuccessful()) {
			throw new NotFoundMemberRankNameException();
		}

		if (Objects.isNull(memberInfoDTO.getBody())) {
			throw new NotFoundMemberRankNameException();
		}

		return memberInfoDTO.getBody().getMemberRank().getMemberRankName();
	}

	/**
	 * 회원 정보를 가져오는 메서드
	 */
	public ResponseMemberInfoDTO getMemberInfo(HttpServletRequest request) throws FeignException {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		ResponseEntity<ResponseMemberInfoDTO> memberInfoDTO = memberInfoAdaptor.getMemberInfo(memberId);
		if (!memberInfoDTO.getStatusCode().is2xxSuccessful()) {
			throw new NotFoundMemberInfoException();
		}

		if (Objects.isNull(memberInfoDTO.getBody())) {
			throw new NotFoundMemberInfoException();
		}

		return memberInfoDTO.getBody();
	}

	/**
	 * 회원 정보를 수정하는 메서드
	 */
	public void updateMemberInfo(HttpServletRequest request,
		RequestMemberInfoDTO requestMemberInfoDTO) throws FeignException {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		ResponseEntity<Void> response = memberInfoAdaptor.updateMemberInfo(memberId, requestMemberInfoDTO);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new NotFoundMemberInfoException();
		}
	}

	/**
	 * 회원 탈퇴하는 메서드
	 */
	public void withdrawMember(HttpServletRequest request,
		HttpServletResponse response) throws FeignException {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		ResponseEntity<Void> responseEntity = memberInfoAdaptor.withdrawMember(memberId);
		if (!responseEntity.getStatusCode().is2xxSuccessful()) {
			throw new NotFoundMemberInfoException();
		}

		withdraw(request, response, memberId);
	}

	private void withdraw(HttpServletRequest request, HttpServletResponse response, String memberId) {
		Cookie[] cookies = request.getCookies();
		if (Objects.nonNull(cookies)) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(JwtRule.JWT_ISSUE_HEADER.getValue())) {
					cookie.setValue(null);
					cookie.setPath("/");
					cookie.setHttpOnly(true);
					cookie.setSecure(true);
					cookie.setMaxAge(0);

					response.addCookie(cookie);
					break;
				}
			}
		}

		String refreshKey = JwtRule.REFRESH_PREFIX.getValue() + ":" + memberId;

		redisTemplate.delete(refreshKey);
	}

}
