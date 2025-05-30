package com.nhnacademy.front.review.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewDTO;
import com.nhnacademy.front.review.model.dto.request.RequestUpdateReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewInfoDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseUpdateReviewDTO;
import com.nhnacademy.front.review.service.ReviewService;

@WithMockUser(username = "member", roles = "MEMBER")
@ActiveProfiles("dev")
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

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
	@DisplayName("POST /reviews - 리뷰 작성 테스트")
	void createReview() throws Exception {
		// given
		MockMultipartFile mockFile = new MockMultipartFile("reviewImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());
		doNothing().when(reviewService).createReview(any(RequestCreateReviewDTO.class));

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/reviews")
				.file(mockFile)
				.param("productId", "1")
				.param("customerId", "") // null 처리법
				.param("memberId", "memberId")
				.param("reviewContent", "좋아요")
				.param("reviewGrade", "5")
				.with(csrf()))
			.andExpect(status().isCreated());

		verify(reviewService).createReview(any(RequestCreateReviewDTO.class));
	}

	@Test
	@DisplayName("PUT /reviews/{reviewId} - 리뷰 수정 테스트")
	void updateReview() throws Exception {
		// given
		MockMultipartFile mockFile = new MockMultipartFile("reviewImage", "test-image.jpg", "image/jpeg",
			"dummy image content".getBytes());
		RequestUpdateReviewDTO requestDto = new RequestUpdateReviewDTO("수정된 내용", mockFile);

		ResponseUpdateReviewDTO responseDto = new ResponseUpdateReviewDTO(requestDto.getReviewContent(), "url");
		when(reviewService.updateReview(1L, requestDto)).thenReturn(responseDto);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.multipart("/reviews/{reviewId}", 1L)
				.file(mockFile)
				.param("reviewContent", requestDto.getReviewContent())
				.with(request -> {
					request.setMethod("PUT");
					return request;
				})
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

		verify(reviewService).updateReview(1L, requestDto);
	}

	@Test
	@DisplayName("GET /products/{productId}/reviews - 상품 리뷰 목록 조회")
	void getReviewsByProduct() throws Exception {
		// given
		long productId = 1L;

		List<ResponseReviewPageDTO> reviewPageList = List.of(
			new ResponseReviewPageDTO(1L, 1L, 1L, "name1", "좋아요", 5, "image1", LocalDateTime.now()),
			new ResponseReviewPageDTO(2L, 1L, 2L, "name2", "별로에요", 2, "image2", LocalDateTime.now())
		);

		ResponseReviewInfoDTO reviewInfo = new ResponseReviewInfoDTO(4.5, 2, List.of(0, 1, 0, 0, 1));
		when(reviewService.getReviewInfo(productId)).thenReturn(reviewInfo);

		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		PageResponse<ResponseReviewPageDTO> pageResponse =
			new PageResponse<>(reviewPageList, pageableInfo, true,
				2, 1, 10, 0, sortInfo, true, 2, false);
		when(reviewService.getReviewsByProduct(eq(productId), any(Pageable.class))).thenReturn(pageResponse);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/products/{productId}/reviews", productId)
				.with(csrf()))
			.andExpect(model().attributeExists("reviewsByProduct"))
			.andExpect(model().attributeExists("totalGradeAvg"))
			.andExpect(model().attributeExists("totalCount"))
			.andExpect(model().attributeExists("starCounts"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("review/product-detail"));

		verify(reviewService).getReviewInfo(productId);
		verify(reviewService).getReviewsByProduct(eq(productId), any(Pageable.class));
	}

}
