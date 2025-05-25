package com.nhnacademy.front.home.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.home.dto.response.ResponseHomeMemberNameDTO;

@FeignClient(name = "home-adaptor", url = "${home.member.name.url}")
public interface HomeAdaptor {

	@GetMapping("/{memberId}")
	ResponseEntity<ResponseHomeMemberNameDTO> getHomeMemberName(@PathVariable("memberId") String memberId);

}
