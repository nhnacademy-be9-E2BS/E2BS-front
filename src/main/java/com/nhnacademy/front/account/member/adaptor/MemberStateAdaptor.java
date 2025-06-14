package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberStateDTO;

@FeignClient(name = "gateway-service", contextId = "member-state-adaptor")
public interface MemberStateAdaptor {

	@GetMapping("/api/members/{member-id}/memberstate")
	ResponseEntity<ResponseMemberStateDTO> getMemberState(@PathVariable("member-id") String memberId);

	@GetMapping("/api/members/{member-id}/memberrole")
	ResponseEntity<String> getMemberRole(@PathVariable("member-id") String memberId);

}
