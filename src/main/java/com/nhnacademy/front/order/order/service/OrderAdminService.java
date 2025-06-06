package com.nhnacademy.front.order.order.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.order.adaptor.OrderAdminAdaptor;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderReturnDTO;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderAdminService {
	private final OrderAdminAdaptor orderAdminAdaptor;

	public ResponseEntity<PageResponse<ResponseOrderDTO>> getOrders(Pageable pageable) throws FeignException {
		return orderAdminAdaptor.getOrders(pageable);
	}

	public ResponseEntity<PageResponse<ResponseOrderDTO>> getOrders(Pageable pageable, String stateName) {
		return orderAdminAdaptor.getOrders(pageable, stateName);
	}

	public ResponseEntity<Void> startDelivery(@PathVariable String orderCode) {
		return orderAdminAdaptor.startDelivery(orderCode);
	}

	public ResponseEntity<PageResponse<ResponseOrderReturnDTO>> getReturnOrders(Pageable pageable){
		return orderAdminAdaptor.getReturnOrders(pageable);
	}
}
