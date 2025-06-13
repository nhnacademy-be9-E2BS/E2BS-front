package com.nhnacademy.front.order.wrapper.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.wrapper.adaptor.AdminWrapperAdaptor;
import com.nhnacademy.front.order.wrapper.adaptor.UserWrapperAdaptor;
import com.nhnacademy.front.order.wrapper.exception.WrapperCreateProcessException;
import com.nhnacademy.front.order.wrapper.exception.WrapperGetProcessException;
import com.nhnacademy.front.order.wrapper.exception.WrapperUpdateProcessException;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestModifyWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestRegisterWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestRegisterWrapperMetaDTO;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WrapperService {

	private final UserWrapperAdaptor userWrapperAdaptor;
	private final AdminWrapperAdaptor adminWrapperAdaptor;

	/**
	 * Wrapper를 back - wrapper table에 저장
	 */
	public void createWrapper(RequestRegisterWrapperDTO registerRequest) throws FeignException {
		RequestRegisterWrapperMetaDTO requestMeta = new RequestRegisterWrapperMetaDTO(registerRequest.getWrapperPrice(), registerRequest.getWrapperName(), registerRequest.getWrapperSaleable());
		ResponseEntity<Void> response = adminWrapperAdaptor.postCreateWrapper(requestMeta, registerRequest.getWrapperImage());

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new WrapperCreateProcessException();
		}
	}

	/**
	 * 판매 중인 Wrapper 리스트를 back에서 조회
	 */
	public PageResponse<ResponseWrapperDTO> getWrappersBySaleable(Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseWrapperDTO>> response = userWrapperAdaptor.getWrappersBySaleable(pageable);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new WrapperGetProcessException();
		}
		return response.getBody();
	}

	/**
	 * 모든(판매 여부 상관 없이) Wrapper 리스트를 back에서 조회
	 */
	public PageResponse<ResponseWrapperDTO> getWrappers(Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseWrapperDTO>> response = adminWrapperAdaptor.getWrappers(pageable);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new WrapperGetProcessException();
		}
		return response.getBody();
	}

	/**
	 * Wrapper를 back - wrapper table에서 수정 (판매 여부만 수정 가능)
	 */
	public void updateWrapper(Long wrapperId, RequestModifyWrapperDTO modifyRequest) throws FeignException {
		ResponseEntity<Void> response = adminWrapperAdaptor.putUpdateWrapper(wrapperId, modifyRequest);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new WrapperUpdateProcessException();
		}
	}
}
