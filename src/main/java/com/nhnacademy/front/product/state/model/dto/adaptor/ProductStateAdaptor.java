package com.nhnacademy.front.product.state.model.dto.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;

@FeignClient(name = "gateway-service", contextId = "product-state-service")
public interface ProductStateAdaptor {
	@GetMapping("/api/productState")
	ResponseEntity<List<ResponseProductStateDTO>> getProductStates();
}
