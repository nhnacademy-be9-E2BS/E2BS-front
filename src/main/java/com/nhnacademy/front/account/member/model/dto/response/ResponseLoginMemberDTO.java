package com.nhnacademy.front.account.member.model.dto.response;

import com.nhnacademy.front.account.member.model.domain.RankName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLoginMemberDTO {

	private String memberId;
	private String customerPassword;
	private RankName memberRankName;

}
