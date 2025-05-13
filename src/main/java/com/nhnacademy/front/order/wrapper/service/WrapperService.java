package com.nhnacademy.front.order.wrapper.service;

import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.exception.EmptyRequestException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.wrapper.adaptor.WrapperAdaptor;
import com.nhnacademy.front.order.wrapper.exception.WrapperCreateProcessException;
import com.nhnacademy.front.order.wrapper.exception.WrapperGetProcessException;
import com.nhnacademy.front.order.wrapper.exception.WrapperUpdateProcessException;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestModifyWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestRegisterWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WrapperService {

	private final WrapperAdaptor wrapperAdaptor;

	/**
	 * Wrapper를 back - wrapper table에 저장
	 */
	public void createWrapper(RequestRegisterWrapperDTO registerRequest) {
		if (Objects.isNull(registerRequest)) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
		}

		try {
			ResponseEntity<Void> response = wrapperAdaptor.postCreateWrapper(registerRequest);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new WrapperCreateProcessException("포장지 등록 실패");
			}
		} catch (FeignException ex) {
			throw new WrapperCreateProcessException("포장지 등록 실패");
		}
	}

	/**
	 * 판매 중인 Wrapper 리스트를 back에서 조회
	 */
	public PageResponse<ResponseWrapperDTO> getWrappersBySaleable(Pageable pageable) {
		try {
			ResponseEntity<PageResponse<ResponseWrapperDTO>> response = wrapperAdaptor.getWrappersBySaleable(pageable);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new WrapperGetProcessException("판매 중인 포장지 리스트 조회 실패");
			}
			return response.getBody();
		} catch (FeignException ex) {
			throw new WrapperGetProcessException("판매 중인 포장지 리스트 조회 실패");
		}
	}

	/**
	 * 모든(판매 여부 상관 없이) Wrapper 리스트를 back에서 조회
	 */
	public PageResponse<ResponseWrapperDTO> getWrappers(Pageable pageable) {
		try {
			ResponseEntity<PageResponse<ResponseWrapperDTO>> response = wrapperAdaptor.getWrappers(pageable);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new WrapperGetProcessException("모든 포장지 리스트 조회 실패");
			}
			return response.getBody();
		} catch (FeignException ex) {
			throw new WrapperGetProcessException("모든 포장지 리스트 조회 실패");
		}
	}

	/**
	 * Wrapper를 back - wrapper table에서 수정 (판매 여부만 수정 가능)
	 */
	public void updateWrapper(Long wrapperId, RequestModifyWrapperDTO modifyRequest) {
		if (Objects.isNull(modifyRequest) || Objects.isNull(wrapperId)) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
		}

		try {
			ResponseEntity<Void> response = wrapperAdaptor.putUpdateWrapper(wrapperId, modifyRequest);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new WrapperUpdateProcessException("포장지 정보 수정 실패");
			}
		} catch (FeignException ex) {
			throw new WrapperUpdateProcessException("포장지 정보 수정 실패");
		}
	}
}
