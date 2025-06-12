package com.nhnacademy.front.product.publisher.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

	/**
	 * Publisher를 back - publisher table에 저장
	 */
	public void createPublisher(RequestPublisherDTO request) throws FeignException {
		ResponseEntity<Void> response = publisherAdaptor.postCreatePublisher(request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new PublisherCreateProcessException();
		}
	}

	/**
	 * Publisher 리스트를 back에서 조회
	 */
	public PageResponse<ResponsePublisherDTO> getPublishers(Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponsePublisherDTO>> response = publisherAdaptor.getPublishers(pageable);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new PublisherGetProcessException();
		}
		return response.getBody();
	}

	/**
	 * Publisher를 back - publisher table에서 수정
	 */
	public void updatePublisher(Long publisherId, RequestPublisherDTO request) throws FeignException {
		ResponseEntity<Void> response = publisherAdaptor.putUpdatePublisher(publisherId, request);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new PublisherUpdateProcessException();
		}
	}
}
