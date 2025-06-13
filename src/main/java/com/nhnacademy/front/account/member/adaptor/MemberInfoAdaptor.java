package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.member.model.dto.request.RequestMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;

@FeignClient(name = "member-info-adaptor", url = "${auth.member.url}")
public interface MemberInfoAdaptor {

	@GetMapping("/{member-id}")
	ResponseEntity<ResponseMemberInfoDTO> getMemberInfo(@PathVariable("member-id") String memberId);

	@PutMapping("/{member-id}/info")
	ResponseEntity<Void> updateMemberInfo(@PathVariable("member-id") String memberId,
		@RequestBody RequestMemberInfoDTO requestMemberInfoDTO);

	@PostMapping("/{member-id}/info")
	ResponseEntity<Void> withdrawMember(@PathVariable("member-id") String memberId);

}
