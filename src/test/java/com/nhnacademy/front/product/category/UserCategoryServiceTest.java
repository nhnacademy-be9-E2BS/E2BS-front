package com.nhnacademy.front.product.category;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.product.category.adaptor.UserCategoryAdaptor;
import com.nhnacademy.front.product.category.exception.CategoryGetProcessException;
import com.nhnacademy.front.product.category.exception.CategoryNotFoundException;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryIdsDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class UserCategoryServiceTest {

	@InjectMocks
	private UserCategoryService userCategoryService;

	@Mock
	private UserCategoryAdaptor userCategoryAdaptor;

	@Mock
	private RedisTemplate<String, Object> redisTemplate;

	@Mock
	private ValueOperations<String, Object> valueOperations;

	@Mock
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("get categories to depth 3 - success")
	void get_categories_to_depth_3_success_test() {
		// given
		ResponseCategoryDTO response = new ResponseCategoryDTO(1L, "Category A",
			List.of(new ResponseCategoryDTO(2L, "Category B",
				List.of(new ResponseCategoryDTO(3L, "Category C",
					List.of())))));

		ResponseEntity<List<ResponseCategoryDTO>> responseEntity = new ResponseEntity<>(List.of(response), HttpStatus.OK);
		when(userCategoryAdaptor.getCategoriesToDepth3()).thenReturn(responseEntity);

		// when
		List<ResponseCategoryDTO> result = userCategoryService.getCategoriesToDepth3();

		// then
		assertThat(result).isEqualTo(List.of(response));
		verify(userCategoryAdaptor, times(1)).getCategoriesToDepth3();
	}

	@Test
	@DisplayName("get categories to depth 3 - fail1")
	void get_categories_to_depth_3_fail1_test() {
		// given
		ResponseEntity<List<ResponseCategoryDTO>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(userCategoryAdaptor.getCategoriesToDepth3()).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> userCategoryService.getCategoriesToDepth3())
			.isInstanceOf(CategoryGetProcessException.class);
	}

	@Test
	@DisplayName("get categories to depth 3 - fail2")
	void get_categories_to_depth_3_fail2_test() {
		// given
		when(userCategoryAdaptor.getCategoriesToDepth3()).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> userCategoryService.getCategoriesToDepth3())
			.isInstanceOf(CategoryGetProcessException.class);
	}

	@Test
	@DisplayName("get categories by id - success (cache 존재)")
	void get_categories_by_id_success_withCache_test() {
		// given
		ResponseCategoryDTO category = new ResponseCategoryDTO(1L, "Category A", List.of());
		List<ResponseCategoryDTO> cachedList = List.of(category);

		when(redisTemplate.hasKey("Categories::all")).thenReturn(true);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get("Categories::all")).thenReturn(cachedList);
		when(objectMapper.convertValue(any(), any(TypeReference.class))).thenReturn(cachedList);

		// when
		ResponseCategoryDTO result = userCategoryService.getCategoriesById(1L);

		// then
		assertThat(result.getCategoryId()).isEqualTo(1L);
		verify(redisTemplate).hasKey("Categories::all");
		verify(redisTemplate).opsForValue();
		verify(objectMapper).convertValue(any(), any(TypeReference.class));
	}

	@Test
	@DisplayName("get categories by id - 성공 (Redis 캐시 없음, API 호출)")
	void get_categories_by_id_success_withApiCall_test() {
		// given
		ResponseCategoryDTO category = new ResponseCategoryDTO(1L, "Category A", List.of());
		List<ResponseCategoryDTO> categoryList = List.of(category);
		ResponseEntity<List<ResponseCategoryDTO>> responseEntity = new ResponseEntity<>(categoryList, HttpStatus.OK);

		when(redisTemplate.hasKey("Categories::all")).thenReturn(false);
		when(userCategoryAdaptor.getAllCategories()).thenReturn(responseEntity);

		// when
		ResponseCategoryDTO result = userCategoryService.getCategoriesById(1L);

		// then
		assertThat(result.getCategoryId()).isEqualTo(1L);
		verify(userCategoryAdaptor).getAllCategories();
	}

	@Test
	@DisplayName("get categories by id - fail1")
	void get_categories_by_id_fail1_test() {
		// given
		ResponseCategoryDTO category = new ResponseCategoryDTO(1L, "Category A", List.of());
		List<ResponseCategoryDTO> categoryList = List.of(category);
		ResponseEntity<List<ResponseCategoryDTO>> responseEntity = new ResponseEntity<>(categoryList, HttpStatus.OK);

		when(redisTemplate.hasKey("Categories::all")).thenReturn(false);
		when(userCategoryAdaptor.getAllCategories()).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> userCategoryService.getCategoriesById(999L))
			.isInstanceOf(CategoryNotFoundException.class);
	}

	@Test
	@DisplayName("get categories by id - fail2")
	void get_categories_by_id_fail2_test() {
		// given
		when(redisTemplate.hasKey("Categories::all")).thenReturn(false);
		when(userCategoryAdaptor.getAllCategories()).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> userCategoryService.getCategoriesById(1L))
			.isInstanceOf(CategoryGetProcessException.class);
	}

	@Test
	@DisplayName("get categories by id - fail3")
	void get_categories_by_id_fail3_test() {
		// given
		Long categoryId = 1L;
		ResponseEntity<List<ResponseCategoryDTO>> nullBodyResponse = new ResponseEntity<>(null, HttpStatus.OK);

		when(redisTemplate.hasKey("Categories::all")).thenReturn(false);
		when(userCategoryAdaptor.getAllCategories()).thenReturn(nullBodyResponse);

		// when & then
		assertThatThrownBy(() -> userCategoryService.getCategoriesById(categoryId))
			.isInstanceOf(CategoryGetProcessException.class);
	}

	@Test
	@DisplayName("get categories by id - fail4")
	void get_categories_by_id_fail4_test() {
		// given
		Long categoryId = 1L;
		ResponseEntity<List<ResponseCategoryDTO>> badResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		when(redisTemplate.hasKey("Categories::all")).thenReturn(false);
		when(userCategoryAdaptor.getAllCategories()).thenReturn(badResponse);

		// when & then
		assertThatThrownBy(() -> userCategoryService.getCategoriesById(categoryId))
			.isInstanceOf(CategoryGetProcessException.class);
	}

	@Test
	@DisplayName("get categories by product ids - success")
	void get_category_by_product_ids_success_test() {
		// given
		List<ResponseCategoryIdsDTO> categoryList = List.of(new ResponseCategoryIdsDTO(1L, List.of(10L, 20L)));
		ResponseEntity<List<ResponseCategoryIdsDTO>> response = new ResponseEntity<>(categoryList, HttpStatus.OK);

		when(userCategoryAdaptor.getCategoriesByProductIds(anyList())).thenReturn(response);

		// when
		List<ResponseCategoryIdsDTO> result = userCategoryService.getCategoriesByProductIds(List.of(1L, 2L));

		// then
		assertThat(result).isEqualTo(categoryList);
		verify(userCategoryAdaptor).getCategoriesByProductIds(anyList());
	}

	@Test
	@DisplayName("get categories by product ids - fail1")
	void get_category_by_product_ids_fail1_test() {
		// given
		List<Long> productIds = List.of(1L);
		ResponseEntity<List<ResponseCategoryIdsDTO>> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		when(userCategoryAdaptor.getCategoriesByProductIds(anyList())).thenReturn(response);

		// when & then
		assertThatThrownBy(() -> userCategoryService.getCategoriesByProductIds(productIds))
			.isInstanceOf(CategoryGetProcessException.class);
	}

	@Test
	@DisplayName("get categories by product ids - fail2")
	void get_category_by_product_ids_fail2_test() {
		// given
		List<Long> productIds = List.of(1L);
		when(userCategoryAdaptor.getCategoriesByProductIds(anyList())).thenThrow(FeignException.class);

		// when & then
		assertThatThrownBy(() -> userCategoryService.getCategoriesByProductIds(productIds))
			.isInstanceOf(CategoryGetProcessException.class);
	}
}
