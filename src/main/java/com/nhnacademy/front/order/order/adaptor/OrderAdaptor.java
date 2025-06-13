package com.nhnacademy.front.order.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.request.RequestPaymentApproveDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;

@FeignClient(name = "gateway-service", contextId = "order-service")
public interface OrderAdaptor {

	@PostMapping("/api/orders/create/payment")
	ResponseEntity<ResponseOrderResultDTO> postCreateOrder(@RequestBody RequestOrderWrapperDTO requestOrderWrapperDTO);

	@PostMapping("/api/orders/cancel")
	ResponseEntity<Void> deleteOrder(@RequestParam String orderId);

	@PostMapping("/api/orders/confirm")
	ResponseEntity<Void> confirmOrder(@RequestBody RequestPaymentApproveDTO approveRequest);

	@GetMapping("/api/orders/{orderCode}")
	ResponseEntity<ResponseOrderWrapperDTO> getOrderByOrderCode(@PathVariable String orderCode);

	@GetMapping("/api/orders/customers/orders")
	ResponseEntity<PageResponse<ResponseOrderDTO>> getOrdersByCustomerId(Pageable pageable, @RequestParam long customerId);

	@GetMapping("/api/orders/{orderCode}/reviewed")
	ResponseEntity<Boolean> isReviewedByOrder(@PathVariable String orderCode);

}
