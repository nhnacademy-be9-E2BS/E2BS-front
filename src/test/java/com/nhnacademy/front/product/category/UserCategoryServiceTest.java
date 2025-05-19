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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nhnacademy.front.product.category.adaptor.UserCategoryAdaptor;
import com.nhnacademy.front.product.category.exception.CategoryGetProcessException;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;

@ExtendWith(MockitoExtension.class)
class UserCategoryServiceTest {

	@InjectMocks
	private UserCategoryService userCategoryService;

	@Mock
	private UserCategoryAdaptor userCategoryAdaptor;

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
	@DisplayName("get categories to depth 3 - fail")
	void get_categories_to_depth_3_fail_test() {
		// given
		ResponseEntity<List<ResponseCategoryDTO>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(userCategoryAdaptor.getCategoriesToDepth3()).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> userCategoryService.getCategoriesToDepth3())
			.isInstanceOf(CategoryGetProcessException.class);
	}

	@Test
	@DisplayName("get categories by id - success")
	void get_categories_by_id_success_test() {
		// given
		Long categoryId = 4L;
		ResponseCategoryDTO response = new ResponseCategoryDTO(1L, "Category A",
			List.of(new ResponseCategoryDTO(2L, "Category B",
				List.of(new ResponseCategoryDTO(3L, "Category C",
					List.of())))));

		ResponseEntity<List<ResponseCategoryDTO>> responseEntity = new ResponseEntity<>(List.of(response), HttpStatus.OK);
		when(userCategoryAdaptor.getCategoriesById(categoryId)).thenReturn(responseEntity);

		// when
		List<ResponseCategoryDTO> result = userCategoryService.getCategoriesById(categoryId);

		// then
		assertThat(result).isEqualTo(List.of(response));
		verify(userCategoryAdaptor, times(1)).getCategoriesById(categoryId);
	}

	@Test
	@DisplayName("get categories by id - fail")
	void get_categories_by_id_fail_test() {
		// given
		Long categoryId = 4L;
		ResponseEntity<List<ResponseCategoryDTO>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(userCategoryAdaptor.getCategoriesById(categoryId)).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> userCategoryService.getCategoriesById(categoryId))
			.isInstanceOf(CategoryGetProcessException.class);
	}
}
