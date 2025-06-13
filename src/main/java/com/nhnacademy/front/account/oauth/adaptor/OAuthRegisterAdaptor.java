package com.nhnacademy.front.account.oauth.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.oauth.model.dto.request.RequestOAuthRegisterDTO;

@FeignClient(name = "gateway-service", contextId = "oauth-register-adaptor")
public interface OAuthRegisterAdaptor {

	@PostMapping("/api/oauth/register")
	ResponseEntity<Void> registerOAuth(@RequestBody RequestOAuthRegisterDTO requestOAuthRegisterDTO);

}
