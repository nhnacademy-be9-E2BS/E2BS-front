package com.nhnacademy.front.account.member.model.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRegisterMemberDTO {

	private String memberId;
	private String customerName;
	private String customerPassword;
	private String customerEmail;
	private LocalDate memberBirth;
	private String memberPhone;

}
