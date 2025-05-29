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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.common.page.PageResponse;
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
			List.of(categoryDTO), new ArrayList<>());
		ResponseEntity<ResponseProductReadDTO> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

		when(productAdaptor.getProductById(anyLong())).thenReturn(responseEntity);

		// when
		ResponseProductReadDTO result = productService.getProduct(1L);

		// then
		assertThat(result).isEqualTo(response);
		verify(productAdaptor, times(1)).getProductById(anyLong());
	}

	@Test
	@DisplayName("get product - fail1")
	void get_product_fail1_test() {
		// given
		ResponseEntity<ResponseProductReadDTO> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdaptor.getProductById(anyLong())).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productService.getProduct(1L))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get product - fail2")
	void get_product_fail2_test() {
		// given
		when(productAdaptor.getProductById(anyLong())).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> productService.getProduct(1L))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get products by category id - success")
	void get_products_by_category_id_success_test() {
		// given
		ResponseProductStateDTO productStateDTO = new ResponseProductStateDTO(1L, ProductStateName.SALE.name());
		ResponsePublisherDTO publisherDTO = new ResponsePublisherDTO(1L, "publisher");
		ResponseCategoryDTO categoryADTO = new ResponseCategoryDTO(1L, "category A", null);
		ResponseProductReadDTO responseA = new ResponseProductReadDTO(1L, productStateDTO, publisherDTO, "title A",
			"content A", "description A",
			LocalDate.now(), "978-89-12345-01-1", 10000, 8000, true, 1000, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>());
		ResponseProductReadDTO responseB = new ResponseProductReadDTO(2L, productStateDTO, publisherDTO, "title B",
			"content B", "description B",
			LocalDate.now(), "978-89-12345-01-2", 9000, 7000, false, 500, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>());
		List<ResponseProductReadDTO> dtos = List.of(responseA, responseB);

		Pageable pageable = PageRequest.of(0, 9);
		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		sortInfo.setEmpty(true);
		sortInfo.setSorted(false);
		sortInfo.setUnsorted(true);

		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		pageableInfo.setPageNumber(0);
		pageableInfo.setPageSize(10);
		pageableInfo.setSort(sortInfo);
		pageableInfo.setOffset(0);
		pageableInfo.setPaged(true);
		pageableInfo.setUnpaged(false);

		PageResponse<ResponseProductReadDTO> pageResponse = new PageResponse<>(
			dtos, pageableInfo, true, 2, 1, 10, 0,
			sortInfo, true, 2, false
		);

		ResponseEntity<PageResponse<ResponseProductReadDTO>> response = new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(productAdaptor.getProductsByCategory(pageable, 1L)).thenReturn(response);

		// when
		PageResponse<ResponseProductReadDTO> result = productService.getProductsByCategoryId(pageable, 1L);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(productAdaptor, times(1)).getProductsByCategory(pageable, 1L);
	}

	@Test
	@DisplayName("get products by category id - fail1")
	void get_products_by_category_id_fail1_test() {
		// given
		Pageable pageable = PageRequest.of(0, 9);
		ResponseEntity<PageResponse<ResponseProductReadDTO>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdaptor.getProductsByCategory(pageable, 1L)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productService.getProductsByCategoryId(pageable, 1L))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get products by category id - fail2")
	void get_products_by_category_id_fail2_test() {
		// given
		Pageable pageable = PageRequest.of(0, 9);
		when(productAdaptor.getProductsByCategory(pageable, 1L)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> productService.getProductsByCategoryId(pageable, 1L))
			.isInstanceOf(ProductGetProcessException.class);
	}
}
