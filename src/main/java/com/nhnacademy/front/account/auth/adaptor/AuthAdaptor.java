package com.nhnacademy.front.account.auth.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.model.dto.response.ResponseJwtTokenDTO;

@FeignClient(name = "gateway-service", contextId = "auth-jwt-create-adaptor")
public interface AuthAdaptor {

	@PostMapping("/api/token")
	ResponseEntity<ResponseJwtTokenDTO> postAuth(@RequestBody RequestJwtTokenDTO requestJwtTokenDTO);

}
