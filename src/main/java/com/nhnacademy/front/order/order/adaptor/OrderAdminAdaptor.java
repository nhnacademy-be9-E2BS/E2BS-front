package com.nhnacademy.front.order.order.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;

@FeignClient(name = "order-admin-service", url = "${order.admin}")
public interface OrderAdminAdaptor {
	@GetMapping
	ResponseEntity<PageResponse<ResponseOrderDTO>> getOrders(Pageable pageable);

}
