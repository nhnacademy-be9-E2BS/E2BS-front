package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberEmailDTO;

@FeignClient(name = "gateway-service", contextId = "member-dormant-adaptor")
public interface MemberDormantAdaptor {

	@PostMapping("/api/dormant/members/{member-id}/dooray")
	ResponseEntity<Void> changeDormantMemberStateActive(@PathVariable("member-id") String memberId);

	@GetMapping("/api/dormant/members/{member-id}")
	ResponseEntity<ResponseMemberEmailDTO> getMemberEmail(@PathVariable("member-id") String memberId);

}
