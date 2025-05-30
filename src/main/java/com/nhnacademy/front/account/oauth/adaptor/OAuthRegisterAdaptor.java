package com.nhnacademy.front.account.oauth.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.oauth.model.dto.request.RequestOAuthRegisterDTO;

@FeignClient(name = "oauth-register-adaptor", url = "${oauth.register.url}")
public interface OAuthRegisterAdaptor {

	@PostMapping
	ResponseEntity<Void> registerOAuth(@RequestBody RequestOAuthRegisterDTO requestOAuthRegisterDTO);

}
