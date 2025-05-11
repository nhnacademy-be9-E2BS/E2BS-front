package com.nhnacademy.front.product.publisher.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.product.publisher.adaptor.PublisherAdaptor;
import com.nhnacademy.front.product.publisher.model.dto.request.RequestPublisherDTO;
import com.nhnacademy.front.product.publisher.model.dto.response.PageResponse;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublisherService {

	private final PublisherAdaptor publisherAdaptor;

	/**
	 * Publisher 리스트를 back에서 조회
	 */
	public PageResponse<ResponsePublisherDTO> getPublishers(Pageable pageable) {
		return publisherAdaptor.getPublishers(pageable);
	}

	/**
	 * Publisher를 back - publisher table에 저장
	 */
	public void createPublisher(RequestPublisherDTO request) {
		publisherAdaptor.postCreatePublisher(request);
	}

	/**
	 * Publisher를 back - publisher table에서 수정
	 */
	public void updatePublisher(Long publisherId, RequestPublisherDTO request) {
		publisherAdaptor.putUpdatePublisher(publisherId, request);
	}
}
