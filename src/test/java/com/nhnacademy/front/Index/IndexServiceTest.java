package com.nhnacademy.front.Index;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.elasticsearch.adaptor.ProductSearchAdaptor;
import com.nhnacademy.front.index.adapter.IndexAdaptor;
import com.nhnacademy.front.index.model.dto.response.ResponseMainPageProductDTO;
import com.nhnacademy.front.index.service.impl.IndexServiceImpl;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;

@ExtendWith(MockitoExtension.class)
class IndexServiceTest {

	@InjectMocks
	private IndexServiceImpl indexService;

	@Mock
	private IndexAdaptor indexAdaptor;

	@Mock
	private ProductSearchAdaptor productSearchAdaptor;

	private final List<ResponseMainPageProductDTO> mockList = List.of(
		new ResponseMainPageProductDTO(1L, "title","contributorName","imageUrl", 100,100, "description", "Apub")
	);

	// ========== Best Seller ==========

	@Test
	@DisplayName("getBestSellerProducts - success")
	void getBestSellerProducts_success() {
		when(productSearchAdaptor.getBestProductsByMain())
			.thenReturn(new ResponseEntity<>(mockList, HttpStatus.OK));

		List<ResponseMainPageProductDTO> result = indexService.getBestSellerProducts();

		assertThat(result).isEqualTo(mockList);
	}

	@Test
	@DisplayName("getBestSellerProducts - fail")
	void getBestSellerProducts_fail() {
		when(productSearchAdaptor.getBestProductsByMain())
			.thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		assertThatThrownBy(() -> indexService.getBestSellerProducts())
			.isInstanceOf(ProductGetProcessException.class);
	}

	// ========== Blog Best ==========

	@Test
	@DisplayName("getBlogBestProducts - success")
	void getBlogBestProducts_success() {
		when(indexAdaptor.getBlogBestProducts())
			.thenReturn(new ResponseEntity<>(mockList, HttpStatus.OK));

		List<ResponseMainPageProductDTO> result = indexService.getBlogBestProducts();

		assertThat(result).isEqualTo(mockList);
	}

	@Test
	@DisplayName("getBlogBestProducts - fail")
	void getBlogBestProducts_fail() {
		when(indexAdaptor.getBlogBestProducts())
			.thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		assertThatThrownBy(() -> indexService.getBlogBestProducts())
			.isInstanceOf(ProductGetProcessException.class);
	}

	// ========== New Items ==========

	@Test
	@DisplayName("getNewItemsProducts - success")
	void getNewItemsProducts_success() {
		when(productSearchAdaptor.getNewestProductsByMain())
			.thenReturn(new ResponseEntity<>(mockList, HttpStatus.OK));

		List<ResponseMainPageProductDTO> result = indexService.getNewItemsProducts();

		assertThat(result).isEqualTo(mockList);
	}

	@Test
	@DisplayName("getNewItemsProducts - fail")
	void getNewItemsProducts_fail() {
		when(productSearchAdaptor.getNewestProductsByMain())
			.thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		assertThatThrownBy(() -> indexService.getNewItemsProducts())
			.isInstanceOf(ProductGetProcessException.class);
	}

	// ========== New Special Items ==========

	@Test
	@DisplayName("getNewSpecialItemsProducts - success")
	void getNewSpecialItemsProducts_success() {
		when(indexAdaptor.getNewSpecialItemsProducts())
			.thenReturn(new ResponseEntity<>(mockList, HttpStatus.OK));

		List<ResponseMainPageProductDTO> result = indexService.getNewSpecialItemsProducts();

		assertThat(result).isEqualTo(mockList);
	}

	@Test
	@DisplayName("getNewSpecialItemsProducts - fail")
	void getNewSpecialItemsProducts_fail() {
		when(indexAdaptor.getNewSpecialItemsProducts())
			.thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		assertThatThrownBy(() -> indexService.getNewSpecialItemsProducts())
			.isInstanceOf(ProductGetProcessException.class);
	}
}
