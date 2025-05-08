package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;

@FeignClient(name = "member-service-register", url = "http://e2bs.shop/register")
public interface MemberRegisterAdaptor {

	@PostMapping
	void postRegisterMember(@RequestBody RequestRegisterMemberDTO requestRegisterMemberDTO);

}
