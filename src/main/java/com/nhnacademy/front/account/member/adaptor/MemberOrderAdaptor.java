package com.nhnacademy.front.account.member.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberOrderDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberRecentOrderDTO;

@FeignClient(name = "member-order-adaptor", url = "${auth.member.mypage.url}")
public interface MemberOrderAdaptor {

	@GetMapping("/{member-id}/orders/counts")
	ResponseEntity<ResponseMemberOrderDTO> getMemberOrdersCnt(@PathVariable("member-id") String memberId);

	@GetMapping("/{member-id}/orders")
	ResponseEntity<List<ResponseMemberRecentOrderDTO>> getMemberRecentOrders(
		@PathVariable("member-id") String memberId);

}
