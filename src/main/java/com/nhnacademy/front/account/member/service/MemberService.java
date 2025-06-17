package com.nhnacademy.front.account.member.service;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.member.adaptor.MemberInfoAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberRegisterAdaptor;
import com.nhnacademy.front.account.member.adaptor.MemberStateAdaptor;
import com.nhnacademy.front.account.member.exception.GetMemberStateFailedException;
import com.nhnacademy.front.account.member.exception.RegisterNotEqualsPasswordException;
import com.nhnacademy.front.account.member.exception.RegisterProcessException;
import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberStateDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseRegisterMemberDTO;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRegisterAdaptor memberRegisterAdaptor;
	private final PasswordEncoder passwordEncoder;
	private final MemberInfoAdaptor memberInfoAdaptor;
	private final MemberStateAdaptor memberStateAdaptor;

	/**
	 * 회원가입 정보를 back 레파지토리 member 테이블에 저장하기 위한 메소드
	 */
	public void createMember(RequestRegisterMemberDTO requestRegisterMemberDTO) {
		if (!requestRegisterMemberDTO.getCustomerPassword()
			.equals(requestRegisterMemberDTO.getCustomerPasswordCheck())) {
			throw new RegisterNotEqualsPasswordException();
		}

		String customerPassword = passwordEncoder.encode(requestRegisterMemberDTO.getCustomerPassword());
		String customerPasswordCheck = passwordEncoder.encode(requestRegisterMemberDTO.getCustomerPasswordCheck());
		requestRegisterMemberDTO.setCustomerPassword(customerPassword);
		requestRegisterMemberDTO.setCustomerPasswordCheck(customerPasswordCheck);

		try {
			ResponseRegisterMemberDTO responseRegisterMemberDTO = memberRegisterAdaptor
				.postRegisterMember(requestRegisterMemberDTO);

			if (Objects.isNull(responseRegisterMemberDTO) || Objects.isNull(responseRegisterMemberDTO.getMemberId())) {
				throw new RegisterProcessException();
			}

		} catch (FeignException ex) {
			throw new RegisterProcessException();
		}

	}

	/**
	 * 회원의 이름을 가져오는 메서드
	 */
	public String getMemberName(HttpServletRequest request) throws FeignException {

		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		ResponseEntity<ResponseMemberInfoDTO> memberInfoDTO = memberInfoAdaptor.getMemberInfo(memberId);
		if (!memberInfoDTO.getStatusCode().is2xxSuccessful() || Objects.isNull(memberInfoDTO.getBody())) {
			return null;
		}
		ResponseMemberInfoDTO responseMemberInfoDTO = memberInfoDTO.getBody();

		return responseMemberInfoDTO.getCustomer().getCustomerName();
	}

	/**
	 * 회원 상태 조회 메소드
	 */
	public String getMemberState(String memberId) {
		try {
			ResponseEntity<ResponseMemberStateDTO> response = memberStateAdaptor.getMemberState(memberId);
			if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
				throw new GetMemberStateFailedException();
			}

			return Objects.requireNonNull(response.getBody()).getMemberstate();
		} catch (FeignException ex) {
			throw new GetMemberStateFailedException();
		}
	}

	/**
	 * 회원 역할 조회 메소드
	 */
	public String getMemberRole(String memberId) {
		try {
			ResponseEntity<String> response = memberStateAdaptor.getMemberRole(memberId);
			if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
				throw new GetMemberStateFailedException();
			}

			return response.getBody();
		} catch (FeignException ex) {
			throw new GetMemberStateFailedException();
		}
	}

	public boolean existsMemberByMemberId(String memberId) {
		ResponseEntity<Map<String, Boolean>> response = memberRegisterAdaptor.checkMemberIdDuplicate(memberId);
		if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
			throw new RegisterProcessException();
		}

		return Boolean.TRUE.equals(response.getBody().get("available"));
	}

}
