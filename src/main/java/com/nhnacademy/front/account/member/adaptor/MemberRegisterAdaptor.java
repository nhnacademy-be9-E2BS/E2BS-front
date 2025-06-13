package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseRegisterMemberDTO;

@FeignClient(name = "gateway-service", contextId = "member-register-adaptor")
public interface MemberRegisterAdaptor {

	@PostMapping("/api/members/register")
	ResponseRegisterMemberDTO postRegisterMember(@RequestBody RequestRegisterMemberDTO requestRegisterMemberDTO);

}
