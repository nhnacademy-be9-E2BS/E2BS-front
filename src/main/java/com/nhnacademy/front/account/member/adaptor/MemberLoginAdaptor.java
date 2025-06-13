package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.member.model.dto.request.RequestLoginMemberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseLoginMemberDTO;

@FeignClient(name = "gateway-service", contextId = "member-login-adaptor")
public interface MemberLoginAdaptor {

	@PostMapping("/api/members/login")
	ResponseLoginMemberDTO postLoginMember(@RequestBody RequestLoginMemberDTO requestLoginMemberDTO);

}
