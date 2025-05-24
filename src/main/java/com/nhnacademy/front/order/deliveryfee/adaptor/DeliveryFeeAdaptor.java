package com.nhnacademy.front.order.deliveryfee.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;

@FeignClient(name = "deliveryFee-service", url = "${order.delivery.user.url}")
public interface DeliveryFeeAdaptor {
	@GetMapping
	ResponseEntity<ResponseDeliveryFeeDTO> getCurrentDeliveryFee();
}
