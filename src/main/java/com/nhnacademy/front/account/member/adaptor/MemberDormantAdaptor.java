package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberEmailDTO;

@FeignClient(name = "member-dormant-adaptor", url = "${member.dormant.url}")
public interface MemberDormantAdaptor {

	@PostMapping("/members/{memberId}/dooray")
	ResponseEntity<Void> changeDormantMemberStateActive(@PathVariable("memberId") String memberId);

	@GetMapping("/members/{memberId}")
	ResponseEntity<ResponseMemberEmailDTO> getMemberEmail(@PathVariable("memberId") String memberId);

}
