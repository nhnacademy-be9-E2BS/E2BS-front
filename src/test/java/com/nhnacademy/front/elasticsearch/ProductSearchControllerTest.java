package com.nhnacademy.front.elasticsearch;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.elasticsearch.controller.ProductSearchController;
import com.nhnacademy.front.elasticsearch.model.dto.domain.ProductSortType;
import com.nhnacademy.front.elasticsearch.service.ProductSearchService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.jwt.parser.JwtHasToken;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.state.model.dto.domain.ProductStateName;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;

import jakarta.servlet.http.HttpServletRequest;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = ProductSearchController.class)
@ActiveProfiles("dev")
public class ProductSearchControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductSearchService productSearchService;

	@MockitoBean
	private UserCategoryService userCategoryService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	@DisplayName("검색어로 도서 조회 - sorting")
	void get_products_by_search_sorting_test() throws Exception {
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

		when(productSearchService.getProductsBySearch(any(Pageable.class), anyString(), any(ProductSortType.class), anyString()))
			.thenReturn(pageResponse);

		try (
			MockedStatic<JwtHasToken> mockedHasToken = mockStatic(JwtHasToken.class);
			MockedStatic<JwtGetMemberId> mockedGetMemberId = mockStatic(JwtGetMemberId.class)
		) {
			mockedHasToken.when(() -> JwtHasToken.hasToken(any(HttpServletRequest.class))).thenReturn(true);
			mockedGetMemberId.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class)))
				.thenReturn("mockMemberId");

			// when & then
			mockMvc.perform(get("/books/search")
					.param("page", "0")
					.param("size", "10")
					.param("keyword", keyword)
					.param("sort", sort.toString())
					.accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(view().name("product/search"))
				.andExpect(model().attributeExists("products"))
				.andExpect(model().attributeExists("keyword"))
				.andExpect(model().attributeExists("sort"))
				.andExpect(model().attribute("sort", sort.toString()));
		}
	}

	@Test
	@DisplayName("검색어로 도서 조회 - no sorting")
	void get_products_by_search_no_sorting_test() throws Exception {
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

		String keyword = "title";

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

		when(productSearchService.getProductsBySearch(any(Pageable.class), anyString(), isNull(), anyString()))
			.thenReturn(pageResponse);

		// when & then
		mockMvc.perform(get("/books/search")
				.param("page", "0")
				.param("size", "10")
				.param("keyword", keyword)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(view().name("product/search"))
			.andExpect(model().attributeExists("products"))
			.andExpect(model().attributeExists("keyword"))
			.andExpect(model().attributeExists("sort"))
			.andExpect(model().attribute("sort", ProductSortType.NO_SORT.toString()));
	}

	@Test
	@DisplayName("카테고리 ID로 도서 조회 - sorting")
	void get_products_by_category_sorting_test() throws Exception {
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

		when(productSearchService.getProductsByCategory(any(Pageable.class), anyLong(), any(ProductSortType.class), anyString()))
			.thenReturn(pageResponse);
		when(userCategoryService.getCategoriesById(anyLong())).thenReturn(categoryADTO);

		// when & then
		mockMvc.perform(get("/books/search/category/1")
				.param("page", "0")
				.param("size", "10")
				.param("sort", sort.toString())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(view().name("product/category"))
			.andExpect(model().attributeExists("products"))
			.andExpect(model().attributeExists("rootCategory"))
			.andExpect(model().attributeExists("sort"))
			.andExpect(model().attribute("sort", sort.toString()));
	}

	@Test
	@DisplayName("카테고리 ID로 도서 조회 - no sorting")
	void get_products_by_category_no_sorting_test() throws Exception {
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

		when(productSearchService.getProductsByCategory(any(Pageable.class), anyLong(), isNull(), anyString()))
			.thenReturn(pageResponse);
		when(userCategoryService.getCategoriesById(anyLong())).thenReturn(categoryADTO);

		try (
			MockedStatic<JwtHasToken> mockedHasToken = mockStatic(JwtHasToken.class);
			MockedStatic<JwtGetMemberId> mockedGetMemberId = mockStatic(JwtGetMemberId.class)
		) {
			mockedHasToken.when(() -> JwtHasToken.hasToken(any(HttpServletRequest.class))).thenReturn(true);
			mockedGetMemberId.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class)))
				.thenReturn("mockMemberId");

			// when & then
			mockMvc.perform(get("/books/search/category/1")
					.param("page", "0")
					.param("size", "10")
					.accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(view().name("product/category"))
				.andExpect(model().attributeExists("products"))
				.andExpect(model().attributeExists("rootCategory"))
				.andExpect(model().attributeExists("sort"))
				.andExpect(model().attribute("sort", ProductSortType.NO_SORT.toString()));
		}
	}

	@Test
	@DisplayName("헤더에서 베스트 도서 조회")
	void get_best_products_test() throws Exception {
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

		Pageable pageable = PageRequest.of(0, 10);
		String memberId = "";

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

		Mockito.when(productSearchService.getBestProducts(pageable, memberId)).thenReturn(pageResponse);

		// when & then
		mockMvc.perform(get("/books/search/best")
				.param("page", "0")
				.param("size", "10")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(view().name("product/best-product"))
			.andExpect(model().attributeExists("products"));
	}

	@Test
	@DisplayName("헤더에서 신상 도서 조회")
	void get_newest_products_test() throws Exception {
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

		Pageable pageable = PageRequest.of(0, 10);
		String memberId = "";

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

		Mockito.when(productSearchService.getNewestProducts(pageable, memberId)).thenReturn(pageResponse);

		// when & then
		mockMvc.perform(get("/books/search/newest")
				.param("page", "0")
				.param("size", "10")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(view().name("product/newest-product"))
			.andExpect(model().attributeExists("products"))
			.andExpect(model().attributeExists("todayDate"));
	}
}
