package com.nhnacademy.front.account.oauth.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "payco-login-adaptor", url = "https://id.payco.com/oauth2.0/authorize")
public interface PaycoLoginAdaptor {

	@GetMapping
	ResponseEntity<Void> getPaycoLogin();

}
