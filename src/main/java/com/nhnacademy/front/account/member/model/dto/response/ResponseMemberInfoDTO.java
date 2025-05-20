package com.nhnacademy.front.account.member.model.dto.response;

import java.time.LocalDate;

import com.nhnacademy.front.account.customer.model.domain.Customer;
import com.nhnacademy.front.account.memberrank.model.domain.MemberRank;
import com.nhnacademy.front.account.memberrole.model.domain.MemberRole;
import com.nhnacademy.front.account.memberstate.model.domain.MemberState;
import com.nhnacademy.front.account.socialauth.model.domain.SocialAuth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberInfoDTO {

	private Customer customer;
	private String memberId;
	private LocalDate memberBirth;
	private String memberPhone;
	private LocalDate memberCreatedAt;
	private LocalDate memberLoginLatest;
	private MemberRank memberRank;
	private MemberState memberState;
	private MemberRole memberRole;
	private SocialAuth socialAuth;

}
