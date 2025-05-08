package com.nhnacademy.front.account.member.model.domain;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Security 로그인을 위한 UserDetails 구현
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginProcessMember implements UserDetails {

	private String memberId;
  private String customerPassword;
	private RankName memberRankName;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String role = "ROLE_" + this.memberRankName;

		return Arrays.asList(new SimpleGrantedAuthority(role));
	}

	@Override
	public String getPassword() {
    return customerPassword;
	}

	@Override
	public String getUsername() {
		return memberId;
	}
}
