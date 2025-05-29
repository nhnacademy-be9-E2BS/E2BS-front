package com.nhnacademy.front.account.oauth.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOAuthRegisterDTO {

	private String memberId;
	private String email;
	private String mobile;
	private String name;
	private String birthdayMMdd;

}
