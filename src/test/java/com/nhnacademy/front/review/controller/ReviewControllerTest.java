package com.nhnacademy.front.review.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewDTO;
import com.nhnacademy.front.review.model.dto.request.RequestUpdateReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseMemberReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseUpdateReviewDTO;
import com.nhnacademy.front.review.service.ReviewService;

import jakarta.servlet.http.HttpServletRequest;

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

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;

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
	@DisplayName("GET /mypage/reviews - 회원 리뷰 목록 페이지 반환 테스트")
	void getReviewsByCustomer() throws Exception {
		// given
		String memberId = "user123";
		List<ResponseMemberReviewDTO> memberReviewDTOList = List.of(
			new ResponseMemberReviewDTO(1L, 1L, "thumbnail", "ProductA", "내용", 4, "http:localhost:9000/bbb.jpg", LocalDateTime.now()),
			new ResponseMemberReviewDTO(2L, 2L, "thumbnail", "ProductB", "내용", 4, "http:localhost:9000/bbb.jpg", LocalDateTime.now())
			);

		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		PageResponse<ResponseMemberReviewDTO> result =
			new PageResponse<>(memberReviewDTOList, pageableInfo, true,
				2, 1, 5, 0, sortInfo, true, 2, false);


		when(reviewService.getReviewsByMember(eq(memberId), any(Pageable.class)))
			.thenReturn(result);

		// when & then
		try (MockedStatic<JwtGetMemberId> mockedStatic = mockStatic(JwtGetMemberId.class)) {
			mockedStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class))).thenReturn(memberId);

			mockMvc.perform(get("/mypage/reviews"))
				.andExpect(status().isOk())
				.andExpect(view().name("member/mypage/reviews"))
				.andExpect(model().attributeExists("reviewsByMember"));
		}

		verify(reviewService).getReviewsByMember(eq(memberId), any(Pageable.class));
	}

	@Test
	@DisplayName("GET /reviews/{orderDetailId} - 주문 상세 Id로 리뷰 단건 조회 테스트")
	void getReviewByOrderDetailId() throws Exception {
		ResponseReviewDTO responseDto = new ResponseReviewDTO(1L, 1L, 1L, "리뷰 내용", 4, "http://img.url");

		when(reviewService.findReviewByOrderDetailId(1L)).thenReturn(responseDto);

		mockMvc.perform(get("/reviews/{orderDetailId}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.reviewId").value(1))
			.andExpect(jsonPath("$.reviewContent").value("리뷰 내용"))
			.andExpect(jsonPath("$.reviewImage").value("http://img.url"));
	}

}
