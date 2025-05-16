package com.nhnacademy.front.product.publisher.service;

import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.exception.EmptyRequestException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.publisher.adaptor.PublisherAdaptor;
import com.nhnacademy.front.product.publisher.exception.PublisherCreateProcessException;
import com.nhnacademy.front.product.publisher.exception.PublisherGetProcessException;
import com.nhnacademy.front.product.publisher.exception.PublisherUpdateProcessException;
import com.nhnacademy.front.product.publisher.model.dto.request.RequestPublisherDTO;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublisherService {

	private final PublisherAdaptor publisherAdaptor;
	private static final String REQUEST_VALUE_MISSING_MESSAGE = "요청 값을 받지 못했습니다.";

	/**
	 * Publisher를 back - publisher table에 저장
	 */
	public void createPublisher(RequestPublisherDTO request) {
		if (Objects.isNull(request)) {
			throw new EmptyRequestException(REQUEST_VALUE_MISSING_MESSAGE);
		}

		try {
			ResponseEntity<Void> response = publisherAdaptor.postCreatePublisher(request);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new PublisherCreateProcessException("출판사 등록 실패");
			}
		} catch (FeignException ex) {
			throw new PublisherCreateProcessException("출판사 등록 실패");
		}
	}

	/**
	 * Publisher 리스트를 back에서 조회
	 */
	public PageResponse<ResponsePublisherDTO> getPublishers(Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponsePublisherDTO>> response = publisherAdaptor.getPublishers(pageable);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new PublisherGetProcessException("출판사 리스트 조회 실패");
		}
		return response.getBody();
	}

	/**
	 * Publisher를 back - publisher table에서 수정
	 */
	public void updatePublisher(Long publisherId, RequestPublisherDTO request) {
		if (Objects.isNull(request) || Objects.isNull(publisherId)) {
			throw new EmptyRequestException(REQUEST_VALUE_MISSING_MESSAGE);
		}

		try {
			ResponseEntity<Void> response = publisherAdaptor.putUpdatePublisher(publisherId, request);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new PublisherUpdateProcessException("출판사 정보 수정 실패");
			}
		} catch (FeignException ex) {
			throw new PublisherUpdateProcessException("출판사 정보 수정 실패");
		}
	}
}
