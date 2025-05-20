package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;

@FeignClient(name = "member-info-adaptor", url = "${member.url}")
public interface MemberInfoAdaptor {

	@GetMapping("/{memberId}")
	ResponseMemberInfoDTO getMemberInfo(@PathVariable("memberId") String memberId);

}
