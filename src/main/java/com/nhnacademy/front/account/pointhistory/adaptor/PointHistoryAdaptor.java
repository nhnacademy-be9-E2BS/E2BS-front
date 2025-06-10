package com.nhnacademy.front.account.pointhistory.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.pointhistory.model.dto.response.ResponsePointHistoryDTO;
import com.nhnacademy.front.common.page.PageResponse;

@FeignClient(name = "point-history-service", url = "${auth.member.mypage.url}")
public interface PointHistoryAdaptor {

	@GetMapping("/{member-id}/pointHistory")
	ResponseEntity<PageResponse<ResponsePointHistoryDTO>> getPointHistoryByMemberId(@PathVariable("member-id") String memberId, Pageable pageable);
}
