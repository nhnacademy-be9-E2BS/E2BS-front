package com.nhnacademy.front.order.deliveryfee.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.deliveryfee.adaptor.DeliveryFeeAdaptor;
import com.nhnacademy.front.order.deliveryfee.adaptor.DeliveryFeeAdminAdaptor;
import com.nhnacademy.front.order.deliveryfee.model.dto.request.RequestDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryFeeSevice {
	private final DeliveryFeeAdminAdaptor deliveryFeeAdminAdaptor;

	private final DeliveryFeeAdaptor deliveryFeeAdaptor;

	/**
	 * 관리자 배송비 정책 조회
	 */
	public ResponseEntity<PageResponse<ResponseDeliveryFeeDTO>> getDeliveryFees(Pageable pageable) {
		return deliveryFeeAdminAdaptor.getDeliveryFees(pageable);
	}

	/**
	 * 관리자 배송비 정책 추가
	 */
	public ResponseEntity<Void> CreateDeliveryFee(RequestDeliveryFeeDTO request) {
		return deliveryFeeAdminAdaptor.postDeliveryFee(request);
	}

	/**
	 * 현재 적용 중인 배송비 정책 조회
	 */
	public ResponseDeliveryFeeDTO getCurrentDeliveryFee() {
		return deliveryFeeAdaptor.getCurrentDeliveryFee().getBody();
	}
}
