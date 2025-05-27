package com.nhnacademy.front.account.admin.model.dto.response;

import com.nhnacademy.front.account.memberstate.model.domain.MemberState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAdminSettingsMembersDTO {

	private String memberId;
	private String customerName;
	private String customerEmail;
	private String memberRankName;
	private MemberState memberState;
	private String memberRole;

}
