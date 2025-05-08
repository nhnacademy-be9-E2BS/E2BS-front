package com.nhnacademy.front.account.member.login.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.member.login.model.dto.request.RequestLoginMemberDTO;
import com.nhnacademy.front.account.member.login.model.dto.response.ResponseLoginMemberDTO;

@FeignClient(name = "member-login-user-details-service", url = "http://e2bs.shop/api/login")
public interface MemberLoginAdaptor {

	@PostMapping
	ResponseLoginMemberDTO postLoginMember(@RequestBody RequestLoginMemberDTO requestLoginMemberDTO);

}
