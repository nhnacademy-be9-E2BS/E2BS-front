package com.nhnacademy.front.product.category;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.product.category.controller.AdminCategoryController;
import com.nhnacademy.front.product.category.model.dto.request.RequestCategoryDTO;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;
import com.nhnacademy.front.product.category.service.UserCategoryService;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = AdminCategoryController.class)
@ActiveProfiles("dev")
class AdminCategoryControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AdminCategoryService adminCategoryService;

	@MockitoBean
	private UserCategoryService userCategoryService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("전체 카테고리 리스트 조회")
	void get_categories_test() throws Exception {
		// given
		ResponseCategoryDTO response = new ResponseCategoryDTO(1L, "Category A",
			List.of(new ResponseCategoryDTO(2L, "Category B",
				List.of(new ResponseCategoryDTO(3L, "Category C",
					List.of())))));

		Mockito.when(adminCategoryService.getCategories()).thenReturn(List.of(response));

		// when & then
		mockMvc.perform(get("/admin/settings/categories"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/product/categories"))
			.andExpect(model().attributeExists("categories"));
	}

	@Test
	@DisplayName("카테고리 생성(최상위+하위) - success")
	void create_category_tree_success_test() throws Exception {
		// given & when & then
		mockMvc.perform(post("/admin/settings/categories")
				.param("categories[0].categoryName", "Category A")
				.param("categories[1].categoryName", "Category B")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/settings/categories"));
	}

	@Test
	@DisplayName("카테고리 생성(최상위+하위) - fail")
	void create_category_tree_fail_test() throws Exception {
		// given
		String categoryName = null;

		// when & then
		mockMvc.perform(post("/admin/settings/categories")
				.param("categories[0].categoryName", "Category A")
				.param("categories[1].categoryName", categoryName)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("카테고리 생성(하위) - success")
	void create_child_category_success_test() throws Exception {
		// given
		RequestCategoryDTO request = new RequestCategoryDTO("New Category");

		// when & then
		mockMvc.perform(post("/admin/settings/categories/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("카테고리 생성(하위) - fail")
	void create_child_category_fail_test() throws Exception {
		// given
		RequestCategoryDTO request = new RequestCategoryDTO(null);

		// when & then
		mockMvc.perform(post("/admin/settings/categories/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("카테고리 수정 - success")
	void update_category_success_test() throws Exception {
		// given
		RequestCategoryDTO request = new RequestCategoryDTO("Update Category");

		// when & then
		mockMvc.perform(put("/admin/settings/categories/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("카테고리 수정 - fail")
	void update_category_fail_test() throws Exception {
		// given
		RequestCategoryDTO request = new RequestCategoryDTO(null);

		// when & then
		mockMvc.perform(put("/admin/settings/categories/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("카테고리 삭제")
	void delete_category_test() throws Exception {
		// when & then
		mockMvc.perform(delete("/admin/settings/categories/1")
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("헤더 카테고리명 조회")
	void get_header_categories_test() throws Exception {
		// Given
		List<ResponseCategoryDTO> headerCategories = List.of(
			new ResponseCategoryDTO(1L, "Category A", null),
			new ResponseCategoryDTO(2L, "Category B", null)
		);
		given(userCategoryService.getCategoriesToDepth3()).willReturn(headerCategories);

		// When & Then
		mockMvc.perform(get("/admin/settings/categories"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("headerCategories", headerCategories));
	}
}
