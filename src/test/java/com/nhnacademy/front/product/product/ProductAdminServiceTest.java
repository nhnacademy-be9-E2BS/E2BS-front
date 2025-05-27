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
import com.nhnacademy.front.product.product.adaptor.ProductAdminAdaptor;
import com.nhnacademy.front.product.product.exception.ProductCreateProcessException;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;
import com.nhnacademy.front.product.product.exception.ProductUpdateProcessException;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductStockUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductCouponDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.impl.ProductAdminServiceImpl;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.state.model.dto.domain.ProductStateName;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;

@ExtendWith(MockitoExtension.class)
class ProductAdminServiceTest {
	@InjectMocks
	private ProductAdminServiceImpl productAdminService;
	@Mock
	private ProductAdminAdaptor productAdminAdaptor;

	@Test
	@DisplayName("create product - success")
	void create_product_success_test() {
		// given
		RequestProductDTO request = new RequestProductDTO(
			1L, 1L, "title", "content", "description", LocalDate.now(),
			"978-89-12345-01-1", 10000L, 8000L, true, 100,
			List.of("a.png", "b.png"), List.of(1L), List.of(1L), List.of(1L));
		when(productAdminAdaptor.postCreateProduct(request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		productAdminService.createProduct(request);

		// then
		verify(productAdminAdaptor, times(1)).postCreateProduct(request);
	}

	@Test
	@DisplayName("create product - fail")
	void create_product_fail_test() {
		// given
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdminAdaptor.postCreateProduct(any())).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productAdminService.createProduct(any()))
			.isInstanceOf(ProductCreateProcessException.class);
	}

	@Test
	@DisplayName("get products - success")
	void get_products_success_test() {
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
			dtos, pageableInfo, true, 2, 1, 9, 0,
			sortInfo, true, 2, false
		);

		ResponseEntity<PageResponse<ResponseProductReadDTO>> response = new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(productAdminAdaptor.getProducts(pageable)).thenReturn(response);

		// when
		PageResponse<ResponseProductReadDTO> result = productAdminService.getProducts(pageable);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(productAdminAdaptor, times(1)).getProducts(pageable);
	}

	@Test
	@DisplayName("get products - fail")
	void get_products_fail_test() {
		// given
		Pageable pageable = PageRequest.of(0, 9);
		ResponseEntity<PageResponse<ResponseProductReadDTO>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdminAdaptor.getProducts(pageable)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productAdminService.getProducts(pageable))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("get products order - success")
	void get_products_order_success_test() {
		// given
		ResponseProductStateDTO productStateDTO = new ResponseProductStateDTO(1L, ProductStateName.SALE.name());
		ResponsePublisherDTO publisherDTO = new ResponsePublisherDTO(1L, "publisher");
		ResponseCategoryDTO categoryADTO = new ResponseCategoryDTO(1L, "category A", null);
		ResponseCategoryDTO categoryBDTO = new ResponseCategoryDTO(2L, "category B", null);
		ResponseProductReadDTO responseA = new ResponseProductReadDTO(1L, productStateDTO, publisherDTO, "title A",
			"content A", "description A",
			LocalDate.now(), "978-89-12345-01-1", 10000, 8000, true, 1000, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>());
		ResponseProductReadDTO responseB = new ResponseProductReadDTO(2L, productStateDTO, publisherDTO, "title B",
			"content B", "description B",
			LocalDate.now(), "978-89-12345-01-2", 9000, 7000, false, 500, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>());
		ResponseProductReadDTO responseC = new ResponseProductReadDTO(3L, productStateDTO, publisherDTO, "title C",
			"content C", "description C",
			LocalDate.now(), "978-89-12345-01-3", 6000, 5000, true, 700, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryBDTO), new ArrayList<>());
		List<ResponseProductReadDTO> dtos = List.of(responseA, responseB, responseC);

		List<Long> productIds = List.of(1L, 2L, 3L);
		ResponseEntity<List<ResponseProductReadDTO>> response = new ResponseEntity<>(dtos, HttpStatus.OK);

		when(productAdminAdaptor.getProducts(productIds)).thenReturn(response);

		// when
		List<ResponseProductReadDTO> result = productAdminService.getProducts(productIds);

		// then
		assertThat(result).isEqualTo(dtos);
		verify(productAdminAdaptor, times(1)).getProducts(productIds);
	}

	@Test
	@DisplayName("get products order - fail")
	void get_products_order_fail_test() {
		// given
		List<Long> productIds = List.of(1L, 2L, 3L);
		ResponseEntity<List<ResponseProductReadDTO>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdminAdaptor.getProducts(productIds)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productAdminService.getProducts(productIds))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("update product - success")
	void update_product_success_test() {
		// given
		RequestProductDTO request = new RequestProductDTO(
			1L, 1L, "title", "content", "description", LocalDate.now(),
			"978-89-12345-01-1", 10000L, 8000L, true, 200,
			List.of("a.png", "b.png"), List.of(1L), List.of(1L), List.of(1L));
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
		when(productAdminAdaptor.putUpdateProduct(1L, request)).thenReturn(responseEntity);

		// when
		productAdminService.updateProduct(1L, request);

		// then
		verify(productAdminAdaptor, times(1)).putUpdateProduct(1L, request);
	}

	@Test
	@DisplayName("update product - fail")
	void update_product_fail_test() {
		// given
		RequestProductDTO request = new RequestProductDTO(
			1L, 1L, "title", "content", "description", LocalDate.now(),
			"978-89-12345-01-1", 10000L, 8000L, true, 200,
			List.of("a.png", "b.png"), List.of(1L), List.of(1L), List.of(1L));
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdminAdaptor.putUpdateProduct(1L, request)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productAdminService.updateProduct(1L, request))
			.isInstanceOf(ProductUpdateProcessException.class);
	}

	@Test
	@DisplayName("update prouct stock - success")
	void update_productStock_success_test() {
		// given
		RequestProductStockUpdateDTO request = new RequestProductStockUpdateDTO(300);
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
		when(productAdminAdaptor.putUpdateProductStock(1L, request)).thenReturn(responseEntity);

		// when
		productAdminService.updateProductStock(1L, request);

		// then
		verify(productAdminAdaptor, times(1)).putUpdateProductStock(1L, request);
	}

	@Test
	@DisplayName("update prouct stock - fail")
	void update_productStock_fail_test() {
		// given
		RequestProductStockUpdateDTO request = new RequestProductStockUpdateDTO(300);
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdminAdaptor.putUpdateProductStock(1L, request)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productAdminService.updateProductStock(1L, request))
			.isInstanceOf(ProductUpdateProcessException.class);
	}

	@Test
	@DisplayName("update product sale price - success")
	void update_productSalePrice_success_test() {
		// given
		RequestProductSalePriceUpdateDTO request = new RequestProductSalePriceUpdateDTO(5000L);
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);
		when(productAdminAdaptor.putUpdateProductSalePrice(1L, request)).thenReturn(responseEntity);

		// when
		productAdminService.updateProductSalePrice(1L, request);

		// then
		verify(productAdminAdaptor, times(1)).putUpdateProductSalePrice(1L, request);
	}

	@Test
	@DisplayName("update product sale price - fail")
	void update_productSalePrice_fail_test() {
		// given
		RequestProductSalePriceUpdateDTO request = new RequestProductSalePriceUpdateDTO(5000L);
		ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdminAdaptor.putUpdateProductSalePrice(1L, request)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productAdminService.updateProductSalePrice(1L, request))
			.isInstanceOf(ProductUpdateProcessException.class);
	}

	@Test
	@DisplayName("get products to coupon - success")
	void get_products_to_coupon_success_test() {
		// given
		ResponseProductCouponDTO responseA = new ResponseProductCouponDTO(1L, "title A", "name A");
		ResponseProductCouponDTO responseB = new ResponseProductCouponDTO(2L, "title B", "name B");
		ResponseProductCouponDTO responseC = new ResponseProductCouponDTO(3L, "title C", "name C");
		List<ResponseProductCouponDTO> dtos = List.of(responseA, responseB, responseC);

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

		PageResponse<ResponseProductCouponDTO> pageResponse = new PageResponse<>(
			dtos, pageableInfo, true, 2, 1, 10, 0,
			sortInfo, true, 2, false
		);

		ResponseEntity<PageResponse<ResponseProductCouponDTO>> response = new ResponseEntity<>(pageResponse, HttpStatus.OK);

		when(productAdminAdaptor.getProductsToCoupon(pageable)).thenReturn(response);

		// when
		PageResponse<ResponseProductCouponDTO> result = productAdminService.getProductsToCoupon(pageable);

		// then
		assertThat(result).isEqualTo(pageResponse);
		verify(productAdminAdaptor, times(1)).getProductsToCoupon(pageable);
	}

	@Test
	@DisplayName("get products to coupon - fail")
	void get_products_to_coupon_fail_test() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		ResponseEntity<PageResponse<ResponseProductCouponDTO>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdminAdaptor.getProductsToCoupon(pageable)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productAdminService.getProductsToCoupon(pageable))
			.isInstanceOf(ProductGetProcessException.class);
	}
}
