package com.nhnacademy.front.order.deliveryfee.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.deliveryfee.model.dto.request.RequestDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;

@FeignClient(name = "gateway-service", contextId = "deliveryFee-admin-service")
public interface DeliveryFeeAdminAdaptor {

	@GetMapping("/api/auth/admin/deliveryFee")
	ResponseEntity<PageResponse<ResponseDeliveryFeeDTO>> getDeliveryFees(Pageable pageable);

	@PostMapping("/api/auth/admin/deliveryFee")
	ResponseEntity<Void> postDeliveryFee(@RequestBody RequestDeliveryFeeDTO request);
}
