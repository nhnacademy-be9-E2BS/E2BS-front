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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.product.adaptor.ProductAdminAdaptor;
import com.nhnacademy.front.product.product.exception.ProductCreateProcessException;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;
import com.nhnacademy.front.product.product.exception.ProductUpdateProcessException;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductMetaDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
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
		MockMultipartFile mockFile1 = new MockMultipartFile("reviewImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());
		MockMultipartFile mockFile2 = new MockMultipartFile("reviewImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());
		List<MultipartFile> productImage = List.of(mockFile1, mockFile2);

		RequestProductDTO request = new RequestProductDTO(
			1L, 1L, "title", "content", "description", LocalDate.now(),
			"978-89-12345-01-1", 10000L, 8000L, true, 100, productImage,
			List.of(1L), List.of(1L), List.of(1L));

		when(productAdminAdaptor.postCreateProduct(any(RequestProductMetaDTO.class), anyList()))
			.thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		productAdminService.createProduct(request);

		// then
		verify(productAdminAdaptor, times(1)).postCreateProduct(any(RequestProductMetaDTO.class), anyList());
	}

	@Test
	@DisplayName("create product - fail")
	void create_product_fail_test() {
		// given
		MockMultipartFile mockFile1 = new MockMultipartFile("reviewImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());
		MockMultipartFile mockFile2 = new MockMultipartFile("reviewImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());
		List<MultipartFile> productImage = List.of(mockFile1, mockFile2);

		RequestProductDTO request = new RequestProductDTO(
			1L, 1L, "title", "content", "description", LocalDate.now(),
			"978-89-12345-01-1", 10000L, 8000L, true, 100, productImage,
			List.of(1L), List.of(1L), List.of(1L));

		when(productAdminAdaptor.postCreateProduct(any(RequestProductMetaDTO.class), anyList()))
			.thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> productAdminService.createProduct(request))
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
			List.of(categoryADTO), new ArrayList<>(), 3.0, 2, false, 10);
		ResponseProductReadDTO responseB = new ResponseProductReadDTO(2L, productStateDTO, publisherDTO, "title B",
			"content B", "description B",
			LocalDate.now(), "978-89-12345-01-2", 9000, 7000, false, 500, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>(), 4.3, 7, false, 10);
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

		ResponseEntity<PageResponse<ResponseProductReadDTO>> response = new ResponseEntity<>(pageResponse,
			HttpStatus.OK);

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
		ResponseEntity<PageResponse<ResponseProductReadDTO>> responseEntity = new ResponseEntity<>(
			HttpStatus.INTERNAL_SERVER_ERROR);
		when(productAdminAdaptor.getProducts(pageable)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> productAdminService.getProducts(pageable))
			.isInstanceOf(ProductGetProcessException.class);
	}

	@Test
	@DisplayName("update product - success")
	void update_product_success_test() {
		// given
		long productId = 1L;
		MockMultipartFile mockFile1 = new MockMultipartFile("productImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());
		MockMultipartFile mockFile2 = new MockMultipartFile("productImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());
		List<MultipartFile> productImage = List.of(mockFile1, mockFile2);

		RequestProductDTO request = new RequestProductDTO(
			1L, 1L, "title", "content", "description", LocalDate.now(),
			"978-89-12345-01-1", 10000L, 8000L, true, 100, productImage,
			List.of(1L), List.of(1L), List.of(1L));

		when(productAdminAdaptor.putUpdateProduct(anyLong(), any(RequestProductMetaDTO.class), anyList()))
			.thenReturn(new ResponseEntity<>(HttpStatus.OK));

		// when
		productAdminService.updateProduct(productId, request);

		// then
		verify(productAdminAdaptor, times(1)).putUpdateProduct(anyLong(), any(RequestProductMetaDTO.class), anyList());
	}

	@Test
	@DisplayName("update product - fail")
	void update_product_fail_test() {
		// given
		MockMultipartFile mockFile1 = new MockMultipartFile("productImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());
		MockMultipartFile mockFile2 = new MockMultipartFile("productImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());
		List<MultipartFile> productImage = List.of(mockFile1, mockFile2);

		RequestProductDTO request = new RequestProductDTO(
			1L, 1L, "title", "content", "description", LocalDate.now(),
			"978-89-12345-01-1", 10000L, 8000L, true, 100, productImage,
			List.of(1L), List.of(1L), List.of(1L));

		when(productAdminAdaptor.putUpdateProduct(anyLong(), any(RequestProductMetaDTO.class), anyList()))
			.thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> productAdminService.updateProduct(1L, request))
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


}
