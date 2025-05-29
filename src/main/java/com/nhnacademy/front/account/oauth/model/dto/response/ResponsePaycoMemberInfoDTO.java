package com.nhnacademy.front.account.oauth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePaycoMemberInfoDTO {

	private String idNo;
	private String email;
	private String mobile;
	private String name;
	private String birthdayMMdd;

}
