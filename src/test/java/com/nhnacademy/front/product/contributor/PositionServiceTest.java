package com.nhnacademy.front.product.contributor;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
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

import com.nhnacademy.front.common.exception.EmptyRequestException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.contributor.position.adaptor.PositionAdaptor;
import com.nhnacademy.front.product.contributor.position.exception.PositionGetProcessException;
import com.nhnacademy.front.product.contributor.position.exception.PositionProcessException;
import com.nhnacademy.front.product.contributor.position.dto.request.RequestPositionDTO;
import com.nhnacademy.front.product.contributor.position.dto.response.ResponsePositionDTO;
import com.nhnacademy.front.product.contributor.position.service.PositionService;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {
	@InjectMocks
	private PositionService positionService;

	@Mock
	private PositionAdaptor positionAdaptor;

	@Test
	@DisplayName("position 생성 - 성공")
	void createPositionSuccess() {
		RequestPositionDTO requestPositionDTO = new RequestPositionDTO("position1");
		ResponsePositionDTO responsePositionDTO = new ResponsePositionDTO(1L, "position1");
		when(positionAdaptor.postCreatePosition(requestPositionDTO)).thenReturn(responsePositionDTO);
		assertThatCode(() -> positionService.createPosition(requestPositionDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("position 생성 - 실패")
	void createPositionFail() {
		RequestPositionDTO requestPositionDTO = null;
		Assertions.assertThatThrownBy(() -> positionService.createPosition(requestPositionDTO)).isInstanceOf(
			EmptyRequestException.class);
	}


	@Test
	@DisplayName("position 생성 - 실패1")
	void createPositionFail2() {
		RequestPositionDTO requestPositionDTO = new RequestPositionDTO("position1");

		when(positionAdaptor.postCreatePosition(requestPositionDTO)).thenThrow(FeignException.class);
		assertThatThrownBy(() -> positionService.createPosition(requestPositionDTO)).isInstanceOf(
			PositionProcessException.class);
	}

	@Test
	@DisplayName("position 수정 성공")
	void updatePositionSuccess() {
		long positionId = 1L;
		RequestPositionDTO requestPositionDTO = new RequestPositionDTO("position1");

		when(positionAdaptor.putUpdatePosition(positionId, requestPositionDTO)).thenReturn(new ResponsePositionDTO(positionId, "position1"));

		positionService.updatePosition(positionId, requestPositionDTO);
		verify(positionAdaptor, times(1)).putUpdatePosition(positionId, requestPositionDTO);
	}

	@Test
	@DisplayName("position 수정 실패")
	void updatePositionFail() {
		RequestPositionDTO requestPositionDTO = new RequestPositionDTO("position1");
		assertThatThrownBy(() -> positionService.updatePosition(null, requestPositionDTO))
			.isInstanceOf(EmptyRequestException.class);
	}

	@Test
	@DisplayName("position 수정 실패1")
	void updatePositionFail1() {
		assertThatThrownBy(() -> positionService.updatePosition(null, null))
			.isInstanceOf(EmptyRequestException.class);
	}

	@Test
	@DisplayName("position 수정 실패2")
	void updatePositionFail2() {
		long positionId = 1L;
		RequestPositionDTO requestPositionDTO = new RequestPositionDTO("position1");

		when(positionAdaptor.putUpdatePosition(positionId, requestPositionDTO)).thenReturn(null);

		assertThatThrownBy(() -> positionService.updatePosition(positionId,requestPositionDTO)).isInstanceOf(
			PositionProcessException.class);
	}

	@Test
	@DisplayName("position 수정 실패3")
	void updatePositionFail3() {
		long positionId = 1L;
		RequestPositionDTO requestPositionDTO = new RequestPositionDTO("position1");

		when(positionAdaptor.putUpdatePosition(positionId, requestPositionDTO)).thenThrow(FeignException.class);

		assertThatThrownBy(() -> positionService.updatePosition(positionId,requestPositionDTO)).isInstanceOf(
			PositionProcessException.class);
	}

	@Test
	@DisplayName("position 단건 조회")
	void getPositionByIdSuccess() {
		long positionId = 1L;
		when(positionService.getPositionById(positionId)).thenReturn(new ResponsePositionDTO(positionId, "position1"));

		assertThatCode(() -> positionService.getPositionById(positionId)).doesNotThrowAnyException();
		assertThat("position1").isEqualTo(positionService.getPositionById(positionId).getPositionName());
	}

	@Test
	@DisplayName("position 단건 조회 실패")
	void getPositionByIdFail() {
		assertThatThrownBy(() -> positionService.getPositionById(null))
			.isInstanceOf(EmptyRequestException.class);
	}

	@Test
	@DisplayName("position 단건 조회 실패1")
	 void getPositionByIdFail1() {
		when(positionAdaptor.getPosition(1L)).thenThrow(FeignException.class);
		assertThatThrownBy(() -> positionService.getPositionById(1L)).isInstanceOf(
			PositionGetProcessException.class);
	}

	@Test
	@DisplayName("position 전체 조회 ")
	void getPositions() {

		ResponsePositionDTO response1 = new ResponsePositionDTO(1L, "position1");
		ResponsePositionDTO response2 = new ResponsePositionDTO(2L, "position2");
		List<ResponsePositionDTO> positions = List.of(response1, response2);

		Pageable pageable = PageRequest.of(0, 10);
		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		sortInfo.setEmpty(true);
		sortInfo.setSorted(false);
		sortInfo.setUnsorted(true);

		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		pageableInfo.setPageNumber(0);
		pageableInfo.setPageSize(10);
		pageableInfo.setSort(sortInfo);
		pageableInfo.setOffset(0);
		pageableInfo.setPaged(true);
		pageableInfo.setUnpaged(false);

		PageResponse<ResponsePositionDTO> pageResponse = new PageResponse<>(
			positions, pageableInfo, true, 2, 1, 10, 0,
			sortInfo, true, 2, false
		);

		ResponseEntity<PageResponse<ResponsePositionDTO>> response = new ResponseEntity<>(pageResponse, HttpStatus.OK);
		when(positionAdaptor.getPositions(pageable)).thenReturn(response);

		PageResponse<ResponsePositionDTO> result = positionService.getPositions(pageable);

		assertThat(result).isNotNull();
		assertThat(result.getTotalElements()).isEqualTo(2);
		assertThat(result).isEqualTo(pageResponse);
		verify(positionAdaptor, times(1)).getPositions(pageable);
	}


	@Test
	@DisplayName("position 전체 조회 실패")
	void getPositionsFail() {
		Pageable pageable = PageRequest.of(0, 10);

		when(positionAdaptor.getPositions(pageable)).thenThrow(FeignException.class);
		assertThatThrownBy(() -> positionService.getPositions(pageable)).isInstanceOf(
			PositionProcessException.class);
	}


	@Test
	@DisplayName("position 리스트 조회 성공")
	void getPositionListSuccess() {
		List<ResponsePositionDTO> dtoList = List.of(
			new ResponsePositionDTO(1L, "position1"),
			new ResponsePositionDTO(2L, "position2")
		);

		PageResponse<ResponsePositionDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(dtoList);
		ResponseEntity<PageResponse<ResponsePositionDTO>> responseEntity = ResponseEntity.ok(pageResponse);

		when(positionAdaptor.getPositions(any())).thenReturn(responseEntity);

		List<ResponsePositionDTO> result = positionService.getPositionList();

		assertEquals(2, result.size());
		assertEquals("position1", result.get(0).getPositionName());
		assertEquals("position2", result.get(1).getPositionName());
	}

	@Test
	@DisplayName("position 리스트 조회 실패")
	void getPositionListFail() {
		when(positionAdaptor.getPositions(any()))
			.thenThrow(FeignException.class);

		assertThrows(PositionGetProcessException.class, () -> {
			positionService.getPositionList();
		});
	}

}
