package com.nhnacademy.front.account.oauth.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.oauth.model.dto.request.RequestOAuthLoginDTO;
import com.nhnacademy.front.account.oauth.model.dto.response.ResponseCheckOAuthIdDTO;

@FeignClient(name = "oauth-adaptor", url = "${oauth.login.url}")
public interface OAuthLoginAdaptor {

	@GetMapping("/{memberId}")
	ResponseEntity<ResponseCheckOAuthIdDTO> checkOAuthLoginId(@PathVariable("memberId") String memberId);

	@PostMapping
	ResponseEntity<Void> loginOAuth(@RequestBody RequestOAuthLoginDTO requestOAuthLoginDTO);

}
