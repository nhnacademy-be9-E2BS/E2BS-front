package com.nhnacademy.front.product.state.model.dto.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.front.product.state.model.dto.adaptor.ProductStateAdaptor;
import com.nhnacademy.front.product.state.model.dto.exception.ProductStateNotFoundException;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductStateService {
	private final ProductStateAdaptor productStateAdaptor;

	/**
	 * ProductState 전체 조회
	 */
	public List<ResponseProductStateDTO> getProductStates() {
		ResponseEntity<List<ResponseProductStateDTO>> response = productStateAdaptor.getProductStates();

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ProductStateNotFoundException("product state not found");
		}
		return response.getBody();
	}
}
