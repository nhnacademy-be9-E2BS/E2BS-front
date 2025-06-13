package com.nhnacademy.front.home.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.home.model.dto.response.ResponseHomeMemberNameDTO;

@FeignClient(name = "gateway-service", contextId = "home-adaptor")
public interface HomeAdaptor {

	@GetMapping("/api/auth/home/{member-id}")
	ResponseEntity<ResponseHomeMemberNameDTO> getHomeMemberName(@PathVariable("member-id") String memberId);

}
