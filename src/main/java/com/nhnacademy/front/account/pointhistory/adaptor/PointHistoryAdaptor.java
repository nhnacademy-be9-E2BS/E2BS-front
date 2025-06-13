package com.nhnacademy.front.account.pointhistory.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.account.pointhistory.model.dto.response.ResponsePointHistoryDTO;
import com.nhnacademy.front.common.page.PageResponse;

@FeignClient(name = "gateway-service", contextId = "point-history-service")
public interface PointHistoryAdaptor {

	@GetMapping("/api/auth/mypage/{member-id}/pointHistory")
	ResponseEntity<PageResponse<ResponsePointHistoryDTO>> getPointHistoryByMemberId(@PathVariable("member-id") String memberId, Pageable pageable);
}
