package com.nhnacademy.front.account.pointhistory.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.account.pointhistory.adaptor.PointHistoryAdaptor;
import com.nhnacademy.front.account.pointhistory.exception.PointHistoryGetException;
import com.nhnacademy.front.account.pointhistory.model.dto.response.ResponsePointHistoryDTO;
import com.nhnacademy.front.account.pointhistory.service.PointHistoryService;
import com.nhnacademy.front.common.page.PageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

	private final PointHistoryAdaptor pointHistoryAdaptor;

	@Override
	public PageResponse<ResponsePointHistoryDTO> getPointHistoryByMemberId(String memberId, Pageable pageable) {
		ResponseEntity<PageResponse<ResponsePointHistoryDTO>> response = pointHistoryAdaptor.getPointHistoryByMemberId(memberId, pageable);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new PointHistoryGetException("회원 아이디로 포인트 내역 조회 실패");
		}
		return response.getBody();
	}
}
