package com.nhnacademy.front.home.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.home.model.dto.response.ResponseHomeMemberNameDTO;

@FeignClient(name = "home-adaptor", url = "${auth.home.member.name.url}")
public interface HomeAdaptor {

	@GetMapping("/{member-id}")
	ResponseEntity<ResponseHomeMemberNameDTO> getHomeMemberName(@PathVariable("member-id") String memberId);

}
