package com.nhnacademy.front.account.pointhistory.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.account.pointhistory.model.dto.response.ResponsePointHistoryDTO;
import com.nhnacademy.front.common.page.PageResponse;

public interface PointHistoryService {

	PageResponse<ResponsePointHistoryDTO> getPointHistoryByMemberId(String memberId, Pageable pageable);
}
