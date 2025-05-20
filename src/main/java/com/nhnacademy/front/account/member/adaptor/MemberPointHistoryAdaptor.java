package com.nhnacademy.front.account.member.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberPointDTO;

@FeignClient(name = "member-point-history-adaptor", url = "${member.mypage.member.url}")
public interface MemberPointHistoryAdaptor {

	@GetMapping("/{memberId}/points")
	ResponseMemberPointDTO getMemberPointAmount(@PathVariable("memberId") String memberId);

}
