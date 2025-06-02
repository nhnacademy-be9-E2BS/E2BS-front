package com.nhnacademy.front.account.oauth.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.account.oauth.model.dto.response.ResponseProviderPaycoAccessTokenDTO;

@FeignClient(name = "oauth-provider-payco-access-token", url = "https://id.payco.com/oauth2.0/token")
public interface OAuthProviderPaycoAccessTokenAdaptor {

	@GetMapping
	ResponseEntity<ResponseProviderPaycoAccessTokenDTO> getPaycoAccessToken(
		@RequestParam("grant_type") String grantType,
		@RequestParam("client_id") String clientId,
		@RequestParam("client_secret") String clientSecret,
		@RequestParam("code") String code
	);

}
