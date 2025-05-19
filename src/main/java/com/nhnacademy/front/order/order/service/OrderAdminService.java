package com.nhnacademy.front.order.order.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.order.adaptor.OrderAdminAdaptor;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderAdminService {
	private final OrderAdminAdaptor orderAdminAdaptor;

	public ResponseEntity<PageResponse<ResponseOrderDTO>> getOrders(Pageable pageable) {
		return orderAdminAdaptor.getOrders(pageable);
	}

	public ResponseEntity<PageResponse<ResponseOrderDTO>> getOrders(Pageable pageable, Long stateId) {
		return orderAdminAdaptor.getOrders(pageable, stateId);
	}

	public ResponseEntity<Void> startDelivery(@PathVariable String orderCode) {
		return orderAdminAdaptor.startDelivery(orderCode);
	}

}
