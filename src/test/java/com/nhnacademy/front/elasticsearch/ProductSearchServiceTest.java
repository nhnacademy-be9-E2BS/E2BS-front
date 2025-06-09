package com.nhnacademy.front.elasticsearch;

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
import com.nhnacademy.front.elasticsearch.adaptor.ProductSearchAdaptor;
import com.nhnacademy.front.elasticsearch.model.dto.domain.ProductSortType;
import com.nhnacademy.front.elasticsearch.service.impl.ProductSearchServiceImpl;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.state.model.dto.domain.ProductStateName;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class ProductSearchServiceTest {
	@InjectMocks
	private ProductSearchServiceImpl productSearchService;

	@Mock
	private ProductSearchAdaptor productSearchAdaptor;

	@Test
	@DisplayName("get products by search - success")
	void get_products_by_search_success_test() {
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

		Pageable pageable = PageRequest.of(0, 10);
		String keyword = "title";
		ProductSortType sort = ProductSortType.LATEST;

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

		ResponseEntity<PageResponse<ResponseProductReadDTO>> response =
			new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(productSearchAdaptor.getProductsBySearch(pageable, keyword, sort)).thenReturn(response);

		// when
		PageResponse<ResponseProductReadDTO> result = productSearchService.getProductsBySearch(pageable, keyword, sort);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(productSearchAdaptor, times(1)).getProductsBySearch(pageable, keyword, sort);
	}

	@Test
	@DisplayName("get products by search - fail1")
	void get_products_by_search_fail1_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		String keyword = "title";
		ProductSortType sort = ProductSortType.LATEST;

		ResponseEntity<PageResponse<ResponseProductReadDTO>> response =
			new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		when(productSearchAdaptor.getProductsBySearch(pageable, keyword, sort)).thenReturn(response);

		// when & then
		assertThatThrownBy(() -> productSearchService.getProductsBySearch(pageable, keyword, sort))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get products by search - fail2")
	void get_products_by_search_fail2_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		String keyword = "title";
		ProductSortType sort = ProductSortType.LATEST;

		when(productSearchAdaptor.getProductsBySearch(pageable, keyword, sort)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> productSearchService.getProductsBySearch(pageable, keyword, sort))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get products by category - success")
	void get_products_by_category_success_test() {
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

		Pageable pageable = PageRequest.of(0, 10);
		Long categoryId = 1L;
		ProductSortType sort = ProductSortType.LATEST;

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

		ResponseEntity<PageResponse<ResponseProductReadDTO>> response =
			new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(productSearchAdaptor.getProductsByCategory(pageable, categoryId, sort)).thenReturn(response);

		// when
		PageResponse<ResponseProductReadDTO> result = productSearchService.getProductsByCategory(pageable, categoryId, sort);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(productSearchAdaptor, times(1)).getProductsByCategory(pageable, categoryId, sort);
	}

	@Test
	@DisplayName("get products by category - fail1")
	void get_products_by_category_fail1_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		Long categoryId = 1L;
		ProductSortType sort = ProductSortType.LATEST;

		ResponseEntity<PageResponse<ResponseProductReadDTO>> response =
			new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		when(productSearchAdaptor.getProductsByCategory(pageable, categoryId, sort)).thenReturn(response);

		// when & then
		assertThatThrownBy(() -> productSearchService.getProductsByCategory(pageable, categoryId, sort))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get products by category - fail2")
	void get_products_by_category_fail2_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		Long categoryId = 1L;
		ProductSortType sort = ProductSortType.LATEST;

		when(productSearchAdaptor.getProductsByCategory(pageable, categoryId, sort)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> productSearchService.getProductsByCategory(pageable, categoryId, sort))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get best products - success")
	void get_best_products_success_test() {
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

		Pageable pageable = PageRequest.of(0, 10);

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

		ResponseEntity<PageResponse<ResponseProductReadDTO>> response =
			new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(productSearchAdaptor.getBestProducts(pageable)).thenReturn(response);

		// when
		PageResponse<ResponseProductReadDTO> result = productSearchService.getBestProducts(pageable);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(productSearchAdaptor, times(1)).getBestProducts(pageable);
	}

	@Test
	@DisplayName("get best products - fail1")
	void get_best_products_fail1_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseProductReadDTO>> response =
			new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		when(productSearchAdaptor.getBestProducts(pageable)).thenReturn(response);

		// when & then
		assertThatThrownBy(() -> productSearchService.getBestProducts(pageable))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get best products - fail2")
	void get_best_products_fail2_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		when(productSearchAdaptor.getBestProducts(pageable)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> productSearchService.getBestProducts(pageable))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get newest products - success")
	void get_newest_products_success_test() {
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

		Pageable pageable = PageRequest.of(0, 10);

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

		ResponseEntity<PageResponse<ResponseProductReadDTO>> response =
			new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(productSearchAdaptor.getNewestProducts(pageable)).thenReturn(response);

		// when
		PageResponse<ResponseProductReadDTO> result = productSearchService.getNewestProducts(pageable);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(productSearchAdaptor, times(1)).getNewestProducts(pageable);
	}

	@Test
	@DisplayName("get newest products - fail1")
	void get_newest_products_fail1_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseProductReadDTO>> response =
			new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		when(productSearchAdaptor.getNewestProducts(pageable)).thenReturn(response);

		// when & then
		assertThatThrownBy(() -> productSearchService.getNewestProducts(pageable))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get newest products - fail2")
	void get_newest_products_fail2_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		when(productSearchAdaptor.getNewestProducts(pageable)).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> productSearchService.getNewestProducts(pageable))
			.isInstanceOf(ProductGetProcessException.class);
	}
}
