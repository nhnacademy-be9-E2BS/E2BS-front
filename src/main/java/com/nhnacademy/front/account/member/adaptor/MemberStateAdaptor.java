package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberStateDTO;

@FeignClient(name = "member-state-adaptor", url = "${member.state.url}")
public interface MemberStateAdaptor {

	@GetMapping("/{memberId}/memberstate")
	ResponseEntity<ResponseMemberStateDTO> getMemberState(@PathVariable("memberId") String memberId);

}
