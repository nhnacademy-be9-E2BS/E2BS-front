package com.nhnacademy.front.order.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;

@FeignClient(name = "order-service", url = "${order.create.url}")
public interface OrderCreateAdaptor {
	@PostMapping("/tossPay")
	ResponseEntity<ResponseOrderResultDTO> postCreateOrder(@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO);

	@PostMapping("/point")
	ResponseEntity<ResponseOrderResultDTO> postPointCreateOrder(
		@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO);
}
