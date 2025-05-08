package com.nhnacademy.front.account.member.login.service;

import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.member.login.adaptor.MemberLoginAdaptor;
import com.nhnacademy.front.account.member.login.exception.LoginProcessException;
import com.nhnacademy.front.account.member.login.model.domain.LoginProcessMember;
import com.nhnacademy.front.account.member.login.model.dto.request.RequestLoginMemberDTO;
import com.nhnacademy.front.account.member.login.model.dto.response.ResponseLoginMemberDTO;

import feign.FeignException;

/**
 * Security 로그인을 위한 UserDetailService 구현
 */
@Service
public class MemberLoginUserDetailsService implements UserDetailsService {

	MemberLoginAdaptor memberLoginAdaptor;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		try {
			ResponseLoginMemberDTO loginMemberDTO = memberLoginAdaptor.postLoginMember(
				new RequestLoginMemberDTO(username));
			if (Objects.isNull(loginMemberDTO)
				|| Objects.isNull(loginMemberDTO.getMemberId())
				|| !loginMemberDTO.getMemberId().equals(username)
				|| Objects.isNull(loginMemberDTO.getMemberPassword())) {
				throw new LoginProcessException("로그인 실패(사용자 정보 불일치)");
			}

			return new LoginProcessMember(loginMemberDTO.getMemberId(),
				loginMemberDTO.getMemberPassword(),
				loginMemberDTO.getMemberRankName());

		} catch (FeignException e) {
			throw new LoginProcessException("로그인 실패");
		}
	}

}
