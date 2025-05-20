package com.nhnacademy.front.account.member.model.dto.response;

import com.nhnacademy.front.account.memberrole.model.domain.MemberRoleName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLoginMemberDTO {

	private String memberId;
	private String customerPassword;
	private MemberRoleName memberRoleName;

}
