package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.member.model.dto.request.RequestLoginMemberDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseLoginMemberDTO;

@FeignClient(name = "member-login-adaptor", url = "${member.login.url}")
public interface MemberLoginAdaptor {

	@PostMapping
	ResponseLoginMemberDTO postLoginMember(@RequestBody RequestLoginMemberDTO requestLoginMemberDTO);

}
