package com.nhnacademy.front.product.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.product.adaptor.ProductAdaptor;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.impl.ProductServiceImpl;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.state.model.dto.domain.ProductStateName;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@InjectMocks
	private ProductServiceImpl productService;
	@Mock
	private ProductAdaptor productAdaptor;

	@Test
	@DisplayName("get product - success")
	void get_product_success_test() {
		// given
		ResponseProductStateDTO productStateDTO = new ResponseProductStateDTO(1L, ProductStateName.SALE.name());
		ResponsePublisherDTO publisherDTO = new ResponsePublisherDTO(1L, "publisher");
		ResponseCategoryDTO categoryDTO = new ResponseCategoryDTO(1L, "category A", null);
		ResponseProductReadDTO response = new ResponseProductReadDTO(1L, productStateDTO, publisherDTO, "title A",
			"content A", "description A",
			LocalDate.now(), "978-89-12345-01-1", 10000, 8000, true, 1000, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryDTO), new ArrayList<>(), 4.5, 5, false, 10);
		ResponseEntity<ResponseProductReadDTO> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

		when(productAdaptor.getProductById(anyLong(), anyString())).thenReturn(responseEntity);

		// when
		ResponseProductReadDTO result = productService.getProduct(1L, "");

		// then
		assertThat(result).isEqualTo(response);
		verify(productAdaptor, times(1)).getProductById(anyLong(), anyString());
	}

	@Test
	@DisplayName("get product - fail1")
	void get_product_fail1_test() {
		// given
		ResponseEntity<ResponseProductReadDTO> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdaptor.getProductById(anyLong(), anyString())).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productService.getProduct(1L, ""))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get product - fail2")
	void get_product_fail2_test() {
		// given
		when(productAdaptor.getProductById(anyLong(), anyString())).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> productService.getProduct(1L, ""))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("order - get products - success")
	void get_products_success_test() {
		// given
		List<Long> productIds = List.of(1L, 2L);
		ResponseProductStateDTO productStateDTO = new ResponseProductStateDTO(1L, ProductStateName.SALE.name());
		ResponsePublisherDTO publisherDTO = new ResponsePublisherDTO(1L, "publisher");
		ResponseCategoryDTO categoryADTO = new ResponseCategoryDTO(1L, "category A", null);
		ResponseProductReadDTO responseA = new ResponseProductReadDTO(1L, productStateDTO, publisherDTO, "title A",
			"content A", "description A",
			LocalDate.now(), "978-89-12345-01-1", 10000, 8000, true, 1000, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>(), 3.0, 2, false, 10);
		ResponseProductReadDTO responseB = new ResponseProductReadDTO(2L, productStateDTO, publisherDTO, "title B",
			"content B", "description B",
			LocalDate.now(), "978-89-12345-01-2", 9000, 7000, false, 500, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>(), 4.3, 7, false, 10);

		List<ResponseProductReadDTO> dtos = List.of(responseA, responseB);
		ResponseEntity<List<ResponseProductReadDTO>> responseEntity = new ResponseEntity<>(dtos, HttpStatus.OK);

		when(productAdaptor.getProducts(any())).thenReturn(responseEntity);

		// when
		List<ResponseProductReadDTO> result = productService.getProducts(productIds);

		// then
		assertThat(result).isEqualTo(dtos);
		verify(productAdaptor, times(1)).getProducts(any());
	}

	@Test
	@DisplayName("order - get products - fail1")
	void get_products_fail1_test() {
		// given
		List<Long> productIds = List.of(1L, 2L);
		ResponseEntity<List<ResponseProductReadDTO>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdaptor.getProducts(any())).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productService.getProducts(productIds))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("order - get products - fail2")
	void get_products_fail2_test() {
		// given
		List<Long> productIds = List.of(1L, 2L);
		when(productAdaptor.getProducts(any())).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> productService.getProducts(productIds))
			.isInstanceOf(ProductGetProcessException.class);
	}
}
