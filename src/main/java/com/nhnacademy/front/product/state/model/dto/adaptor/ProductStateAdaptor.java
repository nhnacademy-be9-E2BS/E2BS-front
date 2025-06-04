package com.nhnacademy.front.product.state.model.dto.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;

@FeignClient(name = "product-state-service", url = "${product.productState.url}")
public interface ProductStateAdaptor {
	@GetMapping
	ResponseEntity<List<ResponseProductStateDTO>> getProductStates();
}
