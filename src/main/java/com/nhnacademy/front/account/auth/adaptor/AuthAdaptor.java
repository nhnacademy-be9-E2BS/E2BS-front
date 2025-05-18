package com.nhnacademy.front.account.auth.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.model.dto.response.ResponseJwtTokenDTO;

@FeignClient(name = "auth-jwt-create-adaptor", url = "${auth.jwt.create.url}")
public interface AuthAdaptor {

	@PostMapping
	ResponseEntity<ResponseJwtTokenDTO> postAuth(@RequestBody RequestJwtTokenDTO requestJwtTokenDTO);

}
