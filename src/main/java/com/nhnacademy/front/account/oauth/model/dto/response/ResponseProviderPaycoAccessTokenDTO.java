package com.nhnacademy.front.account.oauth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProviderPaycoAccessTokenDTO {

	@JsonProperty("access_token_secret")
	private String accessTokenSecret;
	@JsonProperty("state")
	private String state;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("expires_in")
	private String expiresIn;
	@JsonProperty("refresh_token")
	private String refreshToken;
	@JsonProperty("access_token")
	private String accessToken;

}
