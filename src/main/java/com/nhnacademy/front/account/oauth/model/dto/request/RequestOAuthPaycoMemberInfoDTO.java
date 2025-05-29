package com.nhnacademy.front.account.oauth.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOAuthPaycoMemberInfoDTO {

	private String clientId;
	private String accessToken;

}
