package com.nhnacademy.front.home.model.dto.response;

import com.nhnacademy.front.account.memberrole.model.domain.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHomeMemberNameDTO {

	private String memberId;
	private String memberName;
	private MemberRole memberRole;

}
