package com.nhnacademy.front.product.contributor.service;

import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.exception.EmptyRequestException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.contributor.adaptor.PositionAdaptor;
import com.nhnacademy.front.product.contributor.exception.PositionGetProcessException;
import com.nhnacademy.front.product.contributor.exception.PositionProcessException;
import com.nhnacademy.front.product.contributor.model.dto.request.RequestPositionDTO;
import com.nhnacademy.front.product.contributor.model.dto.response.ResponsePositionDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionService {
	private final PositionAdaptor positionAdaptor;

	/**
	 * position 생성
	 */
	public ResponsePositionDTO createPosition(RequestPositionDTO requestPositionDTO) {
		if (Objects.isNull(requestPositionDTO) || Objects.isNull(requestPositionDTO.getPositionName())) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
		}

		try {
			ResponsePositionDTO responsePositionDTO = positionAdaptor.postCreatePosition(requestPositionDTO);
			if (Objects.isNull(responsePositionDTO)) {
				throw new PositionProcessException("position 등록 실패");
			}
			return responsePositionDTO;
		} catch (FeignException e) {
			log.error("FeignException: status={}, content={}", e.status(), e.contentUTF8(), e);
			throw new PositionProcessException("position 등록 실패");
		}
	}

	/**
	 * position 수정
	 */
	public void updatePosition(Long positionId,RequestPositionDTO requestPositionDTO) {
		if (Objects.isNull(requestPositionDTO) || Objects.isNull(positionId) ) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
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
			throw new EmptyRequestException("요청 값을 받지 못했습니다");
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

			if (Objects.isNull(response.getBody())) {
				throw new EmptyRequestException("요청 값을 받지 못했습니다.");
			}

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new PositionProcessException("position 리스트 조회 실패");
			}
			return response.getBody();
		} catch (FeignException e) {
			throw new PositionProcessException("position 리스트 조회 실패");
		}
	}

}
