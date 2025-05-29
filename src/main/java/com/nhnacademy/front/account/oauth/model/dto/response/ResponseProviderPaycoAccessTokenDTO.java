package com.nhnacademy.front.account.oauth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProviderPaycoAccessTokenDTO {

	private String accessToken;
	private String accessTokenSecret;
	private String refreshToken;
	private String tokenType;
	private String expireIn;
	private String state;

}
