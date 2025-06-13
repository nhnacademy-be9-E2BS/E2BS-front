package com.nhnacademy.front.account.memberrank.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.memberrank.model.dto.response.ResponseMemberRankDTO;

@FeignClient(name = "gateway-service", contextId = "member-rank-adaptor")
public interface MemberRankAdaptor {

	@GetMapping("/api/auth/mypage/{member-id}/rank")
	ResponseEntity<List<ResponseMemberRankDTO>> getMemberRank(@PathVariable("member-id") String memberId);

}
