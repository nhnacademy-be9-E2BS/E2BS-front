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

	@GetMapping("/{memberId}")
	ResponseEntity<ResponseMemberInfoDTO> getMemberInfo(@PathVariable("memberId") String memberId);

	@PutMapping("/{memberId}/info")
	ResponseEntity<Void> updateMemberInfo(@PathVariable("memberId") String memberId,
		@RequestBody RequestMemberInfoDTO requestMemberInfoDTO);

	@PostMapping("/{memberId}/info")
	ResponseEntity<Void> withdrawMember(@PathVariable("memberId") String memberId);

}
