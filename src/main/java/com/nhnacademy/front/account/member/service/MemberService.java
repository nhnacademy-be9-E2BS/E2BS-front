package com.nhnacademy.front.account.member.service;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.member.adaptor.MemberRegisterAdaptor;
import com.nhnacademy.front.account.member.exception.RegisterNotEqualsPasswordException;
import com.nhnacademy.front.account.member.exception.RegisterProcessException;
import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseRegisterMemberDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRegisterAdaptor memberRegisterAdaptor;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 회원가입 정보를 back 레파지토리 member 테이블에 저장하기 위한 메소드
	 */
	public void createMember(RequestRegisterMemberDTO requestRegisterMemberDTO) {
		if (!requestRegisterMemberDTO.getCustomerPassword()
			.equals(requestRegisterMemberDTO.getCustomerPasswordCheck())) {
			throw new RegisterNotEqualsPasswordException("입력하신 비밀번호가 서로 같지 않습니다.");
		}

		String customerPassword = passwordEncoder.encode(requestRegisterMemberDTO.getCustomerPassword());
		String customerPasswordCheck = passwordEncoder.encode(requestRegisterMemberDTO.getCustomerPasswordCheck());
		requestRegisterMemberDTO.setCustomerPassword(customerPassword);
		requestRegisterMemberDTO.setCustomerPasswordCheck(customerPasswordCheck);

		try {
			ResponseRegisterMemberDTO responseRegisterMemberDTO = memberRegisterAdaptor
				.postRegisterMember(requestRegisterMemberDTO);

			if (Objects.isNull(responseRegisterMemberDTO) || Objects.isNull(responseRegisterMemberDTO.getMemberId())) {
				throw new RegisterProcessException("회원가입 실패");
			}

		} catch (FeignException ex) {
			throw new RegisterProcessException("회원가입 실패");
		}

	}

}
