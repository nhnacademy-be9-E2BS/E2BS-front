package com.nhnacademy.front.product.product;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;
import com.nhnacademy.front.product.product.controller.ProductController;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductService;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.state.model.dto.domain.ProductStateName;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewInfoDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;
import com.nhnacademy.front.review.service.ReviewService;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = ProductController.class)
@ActiveProfiles("dev")
class ProductControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	@MockitoBean
	private UserCategoryService userCategoryService;

	@MockitoBean
	private ReviewService reviewService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;
	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	@DisplayName("도서 상세 페이지 조회")
	void get_product_test() throws Exception {
		// given
		ResponseProductStateDTO productStateDTO = new ResponseProductStateDTO(1L, ProductStateName.SALE.name());
		ResponsePublisherDTO publisherDTO = new ResponsePublisherDTO(1L, "publisher");
		ResponseCategoryDTO categoryDTO = new ResponseCategoryDTO(1L, "category A", null);
		ResponseProductReadDTO response = new ResponseProductReadDTO(1L, productStateDTO, publisherDTO, "title A",
			"content A", "description A",
			LocalDate.now(), "978-89-12345-01-1", 10000, 8000, true, 1000, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryDTO), new ArrayList<>());

		Mockito.when(productService.getProduct(1L)).thenReturn(response);

		List<ResponseReviewPageDTO> reviewPageList = List.of(
			new ResponseReviewPageDTO(1L, 1L, 1L, "name1", "좋아요", 5, "image1", LocalDateTime.now()),
			new ResponseReviewPageDTO(2L, 1L, 2L, "name2", "별로에요", 2, "image2", LocalDateTime.now())
		);

		ResponseReviewInfoDTO reviewInfo = new ResponseReviewInfoDTO(4.5, 2, List.of(0, 1, 0, 0, 1));
		when(reviewService.getReviewInfo(1L)).thenReturn(reviewInfo);

		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		PageResponse<ResponseReviewPageDTO> pageResponse =
			new PageResponse<>(reviewPageList, pageableInfo, true,
				2, 1, 10, 0, sortInfo, true, 2, false);
		when(reviewService.getReviewsByProduct(eq(1L), any(Pageable.class))).thenReturn(pageResponse);

		// when & then
		mockMvc.perform(get("/books/1"))
			.andExpect(model().attributeExists("product"))
			.andExpect(model().attributeExists("reviewsByProduct"))
			.andExpect(model().attributeExists("totalGradeAvg"))
			.andExpect(model().attributeExists("totalCount"))
			.andExpect(model().attributeExists("starCounts"))
			.andExpect(status().isOk())
			.andExpect(view().name("product/product-detail"));
	}

	@Test
	@DisplayName("카테고리별 도서 리스트 조회")
	void get_product_by_category_id_test() throws Exception {
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

		Mockito.when(productService.getProductsByCategoryId(any(), anyLong())).thenReturn(pageResponse);
		Mockito.when(userCategoryService.getCategoriesById(any())).thenReturn(categoryADTO);

		// when & then
		mockMvc.perform(get("/books/category/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("product/category"))
			.andExpect(model().attributeExists("products"))
			.andExpect(model().attributeExists("rootCategory"));
	}
}
