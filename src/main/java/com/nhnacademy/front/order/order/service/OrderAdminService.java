package com.nhnacademy.front.order.order.service;

import java.time.LocalDate;

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

	public ResponseEntity<PageResponse<ResponseOrderDTO>> getOrders(Pageable pageable, String status,
		String startDate, String endDate, String orderCode, String memberId) throws FeignException {
		return orderAdminAdaptor.getOrders(pageable, status, startDate, endDate, orderCode, memberId);
	}

	public ResponseEntity<Void> startDelivery(@PathVariable String orderCode) {
		return orderAdminAdaptor.startDelivery(orderCode);
	}

	public ResponseEntity<PageResponse<ResponseOrderReturnDTO>> getReturnOrders(Pageable pageable){
		return orderAdminAdaptor.getReturnOrders(pageable);
	}
}
