package com.nhnacademy.front.account.member.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.member.adaptor.MemberRegisterAdaptor;
import com.nhnacademy.front.account.member.exception.RegisterNotEqualsPasswordException;
import com.nhnacademy.front.account.member.exception.RegisterProcessException;
import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;
import com.nhnacademy.front.common.exception.EmptyRequestException;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRegisterAdaptor memberRegisterAdaptor;

	/**
	 * 회원가입 정보를 back 레파지토리 member 테이블에 저장하기 위한 메소드
	 */
	public void createMember(RequestRegisterMemberDTO requestRegisterMemberDTO) {
		if (Objects.isNull(requestRegisterMemberDTO)) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
		}

		if (!requestRegisterMemberDTO.getCustomerPassword().equals(
			requestRegisterMemberDTO.getCustomerPasswordCheck()
		)) {
			throw new RegisterNotEqualsPasswordException("입력하신 비밀번호가 서로 같지 않습니다.");
		}

		try {
			memberRegisterAdaptor.postRegisterMember(requestRegisterMemberDTO);

		} catch (FeignException ex) {
			throw new RegisterProcessException("회원가입 실패");
		}

	}

}
