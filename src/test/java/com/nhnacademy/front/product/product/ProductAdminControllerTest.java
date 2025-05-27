package com.nhnacademy.front.product.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;
import com.nhnacademy.front.product.product.controller.ProductAdminController;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductAdminService;
import com.nhnacademy.front.product.product.service.ProductService;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.state.model.dto.domain.ProductStateName;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;
import com.nhnacademy.front.product.tag.service.TagService;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = ProductAdminController.class)
@ActiveProfiles("dev")
class ProductAdminControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductAdminService productAdminService;

	@MockitoBean
	private ProductService productService;

	@MockitoBean
	private AdminCategoryService adminCategoryService;

	@MockitoBean
	private TagService tagService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	// @Test
	// @DisplayName("전체 도서 리스트 조회")
	// void get_products_test() throws Exception {
	// 	// given
	// 	ResponseProductStateDTO productStateDTO = new ResponseProductStateDTO(1L, ProductStateName.SALE.name());
	// 	ResponsePublisherDTO publisherDTO = new ResponsePublisherDTO(1L, "publisher");
	// 	ResponseCategoryDTO categoryADTO = new ResponseCategoryDTO(1L, "category A", null);
	// 	ResponseProductReadDTO responseA = new ResponseProductReadDTO(1L, productStateDTO, publisherDTO, "title A",
	// 		"content A", "description A",
	// 		LocalDate.now(), "978-89-12345-01-1", 10000, 8000, true, 1000, new ArrayList<>(), new ArrayList<>(),
	// 		List.of(categoryADTO), new ArrayList<>());
	// 	ResponseProductReadDTO responseB = new ResponseProductReadDTO(2L, productStateDTO, publisherDTO, "title B",
	// 		"content B", "description B",
	// 		LocalDate.now(), "978-89-12345-01-2", 9000, 7000, false, 500, new ArrayList<>(), new ArrayList<>(),
	// 		List.of(categoryADTO), new ArrayList<>());
	// 	List<ResponseProductReadDTO> dtos = List.of(responseA, responseB);
	//
	// 	PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
	// 	sortInfo.setEmpty(true);
	// 	sortInfo.setSorted(false);
	// 	sortInfo.setUnsorted(true);
	//
	// 	PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
	// 	pageableInfo.setPageNumber(0);
	// 	pageableInfo.setPageSize(10);
	// 	pageableInfo.setSort(sortInfo);
	// 	pageableInfo.setOffset(0);
	// 	pageableInfo.setPaged(true);
	// 	pageableInfo.setUnpaged(false);
	//
	// 	PageResponse<ResponseProductReadDTO> pageResponse = new PageResponse<>(
	// 		dtos, pageableInfo, true, 2, 1, 10, 0,
	// 		sortInfo, true, 2, false
	// 	);
	//
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	Mockito.when(productAdminService.getProducts(pageable)).thenReturn(pageResponse);
	//
	// 	// when & then
	// 	mockMvc.perform(get("/admin/settings/books"))
	// 		.andExpect(status().isOk())
	// 		.andExpect(view().name("admin/product/books/view"))
	// 		.andExpect(model().attributeExists("products"));
	// }

	@Test
	@DisplayName("도서 단일 조회")
	void get_product_by_id_test() throws Exception {
		// given
		ResponseProductStateDTO productStateDTO = new ResponseProductStateDTO(1L, ProductStateName.SALE.name());
		ResponsePublisherDTO publisherDTO = new ResponsePublisherDTO(1L, "publisher");
		ResponseCategoryDTO categoryDTO = new ResponseCategoryDTO(1L, "category A", null);
		ResponseProductReadDTO response = new ResponseProductReadDTO(1L, productStateDTO, publisherDTO, "title A",
			"content A", "description A",
			LocalDate.now(), "978-89-12345-01-1", 10000, 8000, true, 1000, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryDTO), new ArrayList<>());

		Mockito.when(productService.getProduct(1L)).thenReturn(response);

		// when & then
		mockMvc.perform(get("/admin/settings/books/register/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/product/books/register"))
			.andExpect(model().attributeExists("product"));
	}

	// @Test
	// @DisplayName("도서 등록 뷰 이동")
	// void get_register_view_test() throws Exception {
	// 	// when & then
	// 	mockMvc.perform(get("/admin/settings/books/register"))
	// 		.andExpect(status().isOk())
	// 		.andExpect(view().name("admin/product/books/register"));
	// }

	@Test
	@DisplayName("도서 등록 - success")
	void create_product_success_test() throws Exception {
		// given & when & then
		mockMvc.perform(post("/admin/settings/books/register")
				.param("productStateId", "1")
				.param("publisherId", "1")
				.param("productTitle", "title")
				.param("productContent", "content")
				.param("productDescription", "description")
				.param("productPublishedAt", LocalDate.now().toString())
				.param("productIsbn", "978-89-12345-01-1")
				.param("productRegularPrice", "10000")
				.param("productSalePrice", "8000")
				.param("productPackageable", "true")
				.param("productStock", "100")
				.param("productImagePaths", "a.png", "b.png")
				.param("tagIds", "1")
				.param("categoryIds", "1")
				.param("contributorIds", "1")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/product/books/register"));
	}

	@Test
	@DisplayName("도서 등록 - fail")
	void create_product_fail_test() throws Exception {
		// given
		String productTitle = null;

		// when & then
		mockMvc.perform(post("/admin/settings/books/register")
				.param("productStateId", "1")
				.param("publisherId", "1")
				.param("productTitle", productTitle)
				.param("productContent", "content")
				.param("productDescription", "description")
				.param("productPublishedAt", LocalDate.now().toString())
				.param("productIsbn", "978-89-12345-01-1")
				.param("productRegularPrice", "10000")
				.param("productSalePrice", "8000")
				.param("productPackageable", "true")
				.param("productStock", "100")
				.param("productImagePaths", "a.png", "b.png")
				.param("tagIds", "1")
				.param("categoryIds", "1")
				.param("contributorIds", "1")
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("도서 수정 - success")
	void update_product_success_test() throws Exception {
		// given
		RequestProductDTO request = new RequestProductDTO(
			1L, 1L, "title", "content", "description", LocalDate.now(),
			"978-89-12345-01-1", 10000L, 8000L, true, 200,
			List.of("a.png", "b.png"), List.of(1L), List.of(1L), List.of(1L));

		// when & then
		mockMvc.perform(put("/admin/settings/books/register/1")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("도서 수정 - fail")
	void update_product_fail_test() throws Exception {
		// given
		RequestProductDTO request = new RequestProductDTO(
			1L, 1L, null, "content", "description", LocalDate.now(),
			"978-89-12345-01-1", 10000L, 8000L, true, 200,
			List.of("a.png", "b.png"), List.of(1L), List.of(1L), List.of(1L));

		// when & then
		mockMvc.perform(put("/admin/settings/books/register/1")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("도서 판매가 수정 - success")
	void update_productSalePrice_success_test() throws Exception {
		// given
		RequestProductSalePriceUpdateDTO request = new RequestProductSalePriceUpdateDTO(500L);

		// when & then
		mockMvc.perform(put("/admin/settings/books/1/salePrice")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("도서 판매가 수정 - fail")
	void update_productSalePrice_fail_test() throws Exception {
		// given
		RequestProductSalePriceUpdateDTO request = new RequestProductSalePriceUpdateDTO(null);

		// when & then
		mockMvc.perform(put("/admin/settings/books/1/salePrice")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}
}
