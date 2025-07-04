package com.nhnacademy.front.product.contributor.position.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.error.exception.EmptyRequestException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.contributor.position.adaptor.PositionAdaptor;
import com.nhnacademy.front.product.contributor.position.dto.request.RequestPositionDTO;
import com.nhnacademy.front.product.contributor.position.dto.response.ResponsePositionDTO;
import com.nhnacademy.front.product.contributor.position.exception.PositionGetProcessException;
import com.nhnacademy.front.product.contributor.position.exception.PositionProcessException;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionService {
	private final PositionAdaptor positionAdaptor;

	private static final String REQUEST_VALUE_MISSING_MESSAGE = "요청 값을 받지 못했습니다.";

	/**
	 * position 생성
	 */
	public void createPosition(RequestPositionDTO requestPositionDTO) {
		if (Objects.isNull(requestPositionDTO) || Objects.isNull(requestPositionDTO.getPositionName())) {
			throw new EmptyRequestException(REQUEST_VALUE_MISSING_MESSAGE);
		}

		try {
			positionAdaptor.postCreatePosition(requestPositionDTO);
		} catch (FeignException e) {
			log.error("FeignException: status={}, content={}", e.status(), e.contentUTF8(), e);
			throw new PositionProcessException("position 등록 실패");
		}
	}

	/**
	 * position 수정
	 */
	public void updatePosition(Long positionId, RequestPositionDTO requestPositionDTO) {
		if (Objects.isNull(requestPositionDTO) || Objects.isNull(positionId)) {
			throw new EmptyRequestException(REQUEST_VALUE_MISSING_MESSAGE);
		}

		try {
			ResponsePositionDTO responsePositionDTO = positionAdaptor.putUpdatePosition(positionId, requestPositionDTO);
			if (Objects.isNull(responsePositionDTO)) {
				throw new PositionProcessException("position 수정 실패");
			}
		} catch (FeignException e) {
			throw new PositionProcessException("position 수정 실패");
		}
	}

	/**
	 * positionId로 position 단건 조회
	 */

	public ResponsePositionDTO getPositionById(Long positionId) {
		if (positionId == null) {
			throw new EmptyRequestException(REQUEST_VALUE_MISSING_MESSAGE);
		}
		try {
			return positionAdaptor.getPosition(positionId);
		} catch (FeignException e) {
			throw new PositionGetProcessException("position 조회 실패");
		}
	}

	/**
	 * position 전체 조회
	 */
	public PageResponse<ResponsePositionDTO> getPositions(Pageable pageable) {
		try {
			ResponseEntity<PageResponse<ResponsePositionDTO>> response = positionAdaptor.getPositions(pageable);
			return response.getBody();
		} catch (FeignException e) {
			throw new PositionProcessException("position 리스트 조회 실패");
		}
	}

	/**
	 * position 리스트 가져오기
	 */
	public List<ResponsePositionDTO> getPositionList() {
		try {
			ResponseEntity<PageResponse<ResponsePositionDTO>> response = positionAdaptor.getPositions(
				PageRequest.of(0, 100));
			assert response.getBody() != null;
			return Objects.requireNonNull(response.getBody()).getContent();
		} catch (FeignException e) {
			throw new PositionGetProcessException("Feign 오류로 포지션 목록 가져오기 실패");
		}
	}

}
