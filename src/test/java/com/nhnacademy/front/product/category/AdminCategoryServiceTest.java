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

import com.nhnacademy.front.product.category.adaptor.AdminCategoryAdaptor;
import com.nhnacademy.front.product.category.exception.CategoryCreateProcessException;
import com.nhnacademy.front.product.category.exception.CategoryDeleteProcessException;
import com.nhnacademy.front.product.category.exception.CategoryGetProcessException;
import com.nhnacademy.front.product.category.exception.CategoryUpdateProcessException;
import com.nhnacademy.front.product.category.model.dto.request.RequestCategoryDTO;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;

@ExtendWith(MockitoExtension.class)
class AdminCategoryServiceTest {

	@InjectMocks
	private AdminCategoryService adminCategoryService;

	@Mock
	private AdminCategoryAdaptor adminCategoryAdaptor;

	@Test
	@DisplayName("get categories - success")
	void get_categories_success_test() {
		// given
		ResponseCategoryDTO response = new ResponseCategoryDTO(1L, "Category A",
			List.of(new ResponseCategoryDTO(2L, "Category B",
				List.of(new ResponseCategoryDTO(3L, "Category C",
					List.of())))));

		ResponseEntity<List<ResponseCategoryDTO>> responseEntity = new ResponseEntity<>(List.of(response), HttpStatus.OK);
		when(adminCategoryAdaptor.getCategories()).thenReturn(responseEntity);

		// when
		List<ResponseCategoryDTO> result = adminCategoryService.getCategories();

		// then
		assertThat(result).isEqualTo(List.of(response));
		verify(adminCategoryAdaptor, times(1)).getCategories();
	}

	@Test
	@DisplayName("get categories - fail")
	void get_categories_fail_test() {
		// given
		ResponseEntity<List<ResponseCategoryDTO>> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		when(adminCategoryAdaptor.getCategories()).thenReturn(responseEntity);

		// when & then
		assertThatThrownBy(() -> adminCategoryService.getCategories())
			.isInstanceOf(CategoryGetProcessException.class);
	}

	@Test
	@DisplayName("create category tree - success")
	void create_category_tree_success_test() {
		// given
		RequestCategoryDTO requestA = new RequestCategoryDTO("Category A");
		RequestCategoryDTO requestB = new RequestCategoryDTO("Category B");
		List<RequestCategoryDTO> requests = List.of(requestA, requestB);
		when(adminCategoryAdaptor.postCreateCategoryTree(requests)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		adminCategoryService.createCategoryTree(requests);

		// then
		verify(adminCategoryAdaptor, times(1)).postCreateCategoryTree(requests);
	}

	@Test
	@DisplayName("create category tree - fail")
	void create_category_tree_fail_test() {
		// given
		RequestCategoryDTO requestA = new RequestCategoryDTO("Category A");
		RequestCategoryDTO requestB = new RequestCategoryDTO("Category B");
		List<RequestCategoryDTO> requests = List.of(requestA, requestB);
		when(adminCategoryAdaptor.postCreateCategoryTree(requests)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> adminCategoryService.createCategoryTree(requests))
			.isInstanceOf(CategoryCreateProcessException.class);
	}

	@Test
	@DisplayName("create child category - success")
	void create_child_category_success_test() {
		// given
		Long categoryId = 1L;
		RequestCategoryDTO request = new RequestCategoryDTO("Category A");
		when(adminCategoryAdaptor.postCreateChildCategory(categoryId, request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		adminCategoryService.createChildCategory(categoryId, request);

		// then
		verify(adminCategoryAdaptor, times(1)).postCreateChildCategory(categoryId, request);
	}

	@Test
	@DisplayName("create child category - fail")
	void create_child_category_fail_test() {
		// given
		Long categoryId = 1L;
		RequestCategoryDTO request = new RequestCategoryDTO("Category A");
		when(adminCategoryAdaptor.postCreateChildCategory(categoryId, request)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> adminCategoryService.createChildCategory(categoryId, request))
			.isInstanceOf(CategoryCreateProcessException.class);
	}

	@Test
	@DisplayName("update category - success")
	void update_category_success_test() {
		// given
		Long categoryId = 2L;
		RequestCategoryDTO request = new RequestCategoryDTO("Category A");
		when(adminCategoryAdaptor.putUpdateCategory(categoryId, request)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// when
		adminCategoryService.updateCategory(categoryId, request);

		// then
		verify(adminCategoryAdaptor, times(1)).putUpdateCategory(categoryId, request);
	}

	@Test
	@DisplayName("update category - fail")
	void update_category_fail_test() {
		// given
		Long categoryId = 2L;
		RequestCategoryDTO request = new RequestCategoryDTO("Category A");
		when(adminCategoryAdaptor.putUpdateCategory(categoryId, request)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> adminCategoryService.updateCategory(categoryId, request))
			.isInstanceOf(CategoryUpdateProcessException.class);
	}

	@Test
	@DisplayName("delete category - success")
	void delete_category_success_test() {
		// given
		Long categoryId = 3L;
		when(adminCategoryAdaptor.deleteCategory(categoryId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		// when
		adminCategoryService.deleteCategory(categoryId);

		// then
		verify(adminCategoryAdaptor, times(1)).deleteCategory(categoryId);
	}

	@Test
	@DisplayName("delete category - fail")
	void delete_category_fail_test() {
		// given
		Long categoryId = 3L;
		when(adminCategoryAdaptor.deleteCategory(categoryId)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

		// when & then
		assertThatThrownBy(() -> adminCategoryService.deleteCategory(categoryId))
			.isInstanceOf(CategoryDeleteProcessException.class);
	}
}
