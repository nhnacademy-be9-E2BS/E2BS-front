package com.nhnacademy.front.account.member.model.dto.response;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRegisterMemberDTO {

	@NotNull
	private String memberId;
	@NotNull
	private String customerName;
	@NotNull
	private String customerPassword;
	@NotNull
	private String customerEmail;
	@NotNull
	private LocalDate memberBirth;
	@NotNull
	private String memberPhone;

}
