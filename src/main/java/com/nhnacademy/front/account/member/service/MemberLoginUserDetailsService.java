package com.nhnacademy.front.account.member.service;

import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.member.adaptor.MemberLoginAdaptor;
import com.nhnacademy.front.account.member.exception.LoginProcessException;
import com.nhnacademy.front.account.member.model.domain.LoginProcessMember;
import com.nhnacademy.front.account.member.model.dto.request.RequestLoginMemberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseLoginMemberDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

/**
 * Security 로그인을 위한 UserDetailService 구현
 */
@Service
@RequiredArgsConstructor
public class MemberLoginUserDetailsService implements UserDetailsService {

	private final MemberLoginAdaptor memberLoginAdaptor;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		try {
			ResponseLoginMemberDTO loginMemberDTO = memberLoginAdaptor
				.postLoginMember(new RequestLoginMemberDTO(username));

			if (Objects.isNull(loginMemberDTO)
				|| Objects.isNull(loginMemberDTO.getMemberId())
				|| !loginMemberDTO.getMemberId().equals(username)
				|| Objects.isNull(loginMemberDTO.getCustomerPassword())) {
				throw new LoginProcessException("로그인 실패(사용자 정보 불일치)");
			}

			return new LoginProcessMember(loginMemberDTO.getMemberId(),
				loginMemberDTO.getCustomerPassword(),
				loginMemberDTO.getMemberRoleName());

		} catch (FeignException e) {
			throw new LoginProcessException("로그인 실패");
		}
	}

}
