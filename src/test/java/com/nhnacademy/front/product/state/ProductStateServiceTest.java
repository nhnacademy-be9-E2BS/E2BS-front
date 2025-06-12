package com.nhnacademy.front.product.state;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.product.state.model.dto.adaptor.ProductStateAdaptor;
import com.nhnacademy.front.product.state.model.dto.exception.ProductStateNotFoundException;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;
import com.nhnacademy.front.product.state.model.dto.service.ProductStateService;

@ExtendWith(MockitoExtension.class)
class ProductStateServiceTest {

	@Mock
	ProductStateAdaptor productStateAdaptor;

	@InjectMocks
	ProductStateService productStateService;

	@Test
	@DisplayName("getProductStates - 정상 응답")
	void getProductStates_success() {
		// given
		List<ResponseProductStateDTO> mockList = List.of(
			new ResponseProductStateDTO(1L, "SALE"),
			new ResponseProductStateDTO(2L, "OUT")
		);
		ResponseEntity<List<ResponseProductStateDTO>> response =
			new ResponseEntity<>(mockList, HttpStatus.OK);

		given(productStateAdaptor.getProductStates()).willReturn(response);

		// when
		List<ResponseProductStateDTO> result = productStateService.getProductStates();

		// then
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getProductStateName()).isEqualTo("SALE");
	}

	@Test
	@DisplayName("getProductStates - 실패(2xx가 아닐 때 예외 발생)")
	void getProductStates_fail() {
		// given
		ResponseEntity<List<ResponseProductStateDTO>> response =
			new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

		given(productStateAdaptor.getProductStates()).willReturn(response);

		// when & then
		assertThatThrownBy(() -> productStateService.getProductStates())
			.isInstanceOf(ProductStateNotFoundException.class)
			.hasMessageContaining("product state not found");
	}
}
