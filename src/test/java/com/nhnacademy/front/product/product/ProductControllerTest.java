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
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.jwt.parser.JwtHasToken;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.product.controller.ProductController;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductService;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.state.model.dto.domain.ProductStateName;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewInfoDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;
import com.nhnacademy.front.review.service.ReviewService;

import jakarta.servlet.http.HttpServletRequest;

@WithMockUser(username = "admin", roles = "ADMIN")
@WebMvcTest(controllers = ProductController.class)
@ActiveProfiles("dev")
class ProductControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	@MockitoBean
	private ReviewService reviewService;

	@MockitoBean
	private DeliveryFeeSevice deliveryFeeSevice;

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
	@DisplayName("도서 상세 페이지 조회")
	void get_product_test() throws Exception {
		// given
		String memberId = "member";
		ResponseProductStateDTO productStateDTO = new ResponseProductStateDTO(1L, ProductStateName.SALE.name());
		ResponsePublisherDTO publisherDTO = new ResponsePublisherDTO(1L, "publisher");
		ResponseCategoryDTO categoryADTO = new ResponseCategoryDTO(1L, "category A", null);
		ResponseProductReadDTO response = new ResponseProductReadDTO(1L, productStateDTO, publisherDTO, "title A",
			"content A", "description A",
			LocalDate.now(), "978-89-12345-01-1", 10000, 8000, true, 1000, new ArrayList<>(), new ArrayList<>(),
			List.of(categoryADTO), new ArrayList<>(), 3.0, 2, false, 10);

		when(productService.getProduct(anyLong(), anyString())).thenReturn(response);
		when(deliveryFeeSevice.getCurrentDeliveryFee()).thenReturn(new ResponseDeliveryFeeDTO(1L, 3000L, 30000L, LocalDateTime.now()));

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

		try (
			MockedStatic<JwtHasToken> mockedHasToken = mockStatic(JwtHasToken.class);
			MockedStatic<JwtGetMemberId> mockedGetMemberId = mockStatic(JwtGetMemberId.class)
		) {
			mockedHasToken.when(() -> JwtHasToken.hasToken(any(HttpServletRequest.class))).thenReturn(true);
			mockedGetMemberId.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class)))
				.thenReturn("mockMemberId");

			// when & then
			mockMvc.perform(get("/books/1")
					.accept(MediaType.TEXT_HTML))
				.andExpect(model().attributeExists("product"))
				.andExpect(model().attributeExists("reviewsByProduct"))
				.andExpect(model().attributeExists("totalGradeAvg"))
				.andExpect(model().attributeExists("totalCount"))
				.andExpect(model().attributeExists("starCounts"))
				.andExpect(status().isOk())
				.andExpect(view().name("product/product-detail"));
		}
	}
}
