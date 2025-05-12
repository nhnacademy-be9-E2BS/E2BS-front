package com.nhnacademy.front.order.wrapper.service;

import java.awt.print.Pageable;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.exception.EmptyRequestException;
import com.nhnacademy.front.order.wrapper.adaptor.WrapperAdaptor;
import com.nhnacademy.front.order.wrapper.exception.WrapperCreateProcessException;
import com.nhnacademy.front.order.wrapper.exception.WrapperGetProcessException;
import com.nhnacademy.front.order.wrapper.exception.WrapperUpdateProcessException;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WrapperService {

	private final WrapperAdaptor wrapperAdaptor;

	public void createWrapper(RequestWrapperDTO request) {
		if (Objects.isNull(request)) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
		}

		try {
			ResponseEntity<Void> response = wrapperAdaptor.postCreateWrapper(request);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new WrapperCreateProcessException("포장지 등록 실패");
			}
		} catch (FeignException ex) {
			throw new WrapperCreateProcessException("포장지 등록 실패");
		}
	}

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

	public void updateWrapper(Long wrapperId, RequestWrapperDTO request) {
		if (Objects.isNull(request) || Objects.isNull(wrapperId)) {
			throw new EmptyRequestException("요청 값을 받지 못했습니다.");
		}

		try {
			ResponseEntity<Void> response = wrapperAdaptor.putUpdateWrapper(wrapperId, request);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new WrapperUpdateProcessException("포장지 정보 수정 실패");
			}
		} catch (FeignException ex) {
			throw new WrapperUpdateProcessException("포장지 정보 수정 실패");
		}
	}
}
