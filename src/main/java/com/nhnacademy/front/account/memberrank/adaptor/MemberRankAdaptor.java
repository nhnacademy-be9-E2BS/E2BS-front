package com.nhnacademy.front.account.memberrank.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.memberrank.model.dto.response.ResponseMemberRankDTO;

@FeignClient(name = "member-rank-adaptor", url = "${auth.member.mypage.url}")
public interface MemberRankAdaptor {

	@GetMapping("/{memberId}/rank")
	ResponseEntity<List<ResponseMemberRankDTO>> getMemberRank(@PathVariable("memberId") String memberId);

}
