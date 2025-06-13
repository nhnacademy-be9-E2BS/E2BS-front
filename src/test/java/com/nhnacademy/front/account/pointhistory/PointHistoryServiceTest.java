package com.nhnacademy.front.account.pointhistory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.account.pointhistory.adaptor.PointHistoryAdaptor;
import com.nhnacademy.front.account.pointhistory.exception.PointHistoryGetException;
import com.nhnacademy.front.account.pointhistory.model.dto.response.ResponsePointHistoryDTO;
import com.nhnacademy.front.account.pointhistory.service.impl.PointHistoryServiceImpl;
import com.nhnacademy.front.common.page.PageResponse;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceTest {

	@Mock
	private PointHistoryAdaptor pointHistoryAdaptor;

	@InjectMocks
	private PointHistoryServiceImpl pointHistoryService;

	@Test
	@DisplayName("포인트 내역 조회 성공")
	void testGetPointHistorySuccess() {
		// given
		String memberId = "test-member";
		Pageable pageable = PageRequest.of(0, 10);

		PageResponse<ResponsePointHistoryDTO> mockPage = new PageResponse<>();
		mockPage.setContent(List.of(new ResponsePointHistoryDTO()));
		mockPage.setSize(10);

		when(pointHistoryAdaptor.getPointHistoryByMemberId(memberId, pageable))
			.thenReturn(ResponseEntity.ok(mockPage));

		// when
		PageResponse<ResponsePointHistoryDTO> result = pointHistoryService.getPointHistoryByMemberId(memberId, pageable);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(1);
		verify(pointHistoryAdaptor).getPointHistoryByMemberId(memberId, pageable);
	}

	@Test
	@DisplayName("포인트 내역 조회 실패 - 응답 오류")
	void testGetPointHistoryFail() {
		// given
		String memberId = "test-member";
		Pageable pageable = PageRequest.of(0, 10);

		when(pointHistoryAdaptor.getPointHistoryByMemberId(memberId, pageable))
			.thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

		// when & then
		assertThatThrownBy(() -> pointHistoryService.getPointHistoryByMemberId(memberId, pageable))
			.isInstanceOf(PointHistoryGetException.class);

		verify(pointHistoryAdaptor).getPointHistoryByMemberId(memberId, pageable);
	}
}
