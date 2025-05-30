package com.nhnacademy.front.review.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.review.adaptor.ProductReviewAdaptor;
import com.nhnacademy.front.review.adaptor.ReviewAdaptor;
import com.nhnacademy.front.review.exception.ReviewProcessException;
import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewDTO;
import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewMetaDTO;
import com.nhnacademy.front.review.model.dto.request.RequestUpdateReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewInfoDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseUpdateReviewDTO;
import com.nhnacademy.front.review.service.impl.ReviewServiceImpl;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

	@Mock
	private ProductReviewAdaptor productReviewAdaptor;

	@Mock
	private ReviewAdaptor reviewAdaptor;

	@InjectMocks
	private ReviewServiceImpl reviewService;

	
	@Test
	@DisplayName("리뷰 생성 테스트")
	void createReview() {
		// given
		RequestCreateReviewMetaDTO meta = new RequestCreateReviewMetaDTO(1L, null, "memberId", "좋아요", 5);
		MockMultipartFile mockImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", "data".getBytes());
		RequestCreateReviewDTO request = new RequestCreateReviewDTO(1L, null, "memberId", "좋아요", 5, mockImage);

		when(reviewAdaptor.createReview(meta, mockImage))
			.thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

		// when & then
		assertDoesNotThrow(() -> reviewService.createReview(request));
	}

	@Test
	@DisplayName("리뷰 생성 실패 - HTTP 오류 반환")
	void createReview_Fail_ReviewProcessException() {
		// given
		RequestCreateReviewMetaDTO meta = new RequestCreateReviewMetaDTO(1L, null, "memberId", "좋아요", 5);
		MockMultipartFile mockImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", "data".getBytes());
		RequestCreateReviewDTO request = new RequestCreateReviewDTO(1L, null, "memberId", "좋아요", 5, mockImage);

		when(reviewAdaptor.createReview(meta, mockImage))
			.thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).build());

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.createReview(request));
	}

	@Test
	@DisplayName("리뷰 수정 테스트")
	void updateReview() {
		// given
		long reviewId = 1L;
		RequestUpdateReviewDTO request = new RequestUpdateReviewDTO("수정된 내용", null);
		ResponseUpdateReviewDTO responseDto = new ResponseUpdateReviewDTO();

		when(reviewAdaptor.updateReview(eq(reviewId), eq("수정된 내용"), any()))
			.thenReturn(ResponseEntity.ok(responseDto));

		// when
		ResponseUpdateReviewDTO result = reviewService.updateReview(reviewId, request);

		// then
		assertEquals(responseDto, result);
	}

	@Test
	@DisplayName("리뷰 수정 테스트 - 실패(HTTP 오류)")
	void updateReview_Fail_ReviewProcessException() {
		// given
		long reviewId = 1L;
		RequestUpdateReviewDTO request = new RequestUpdateReviewDTO("수정된 내용", null);

		when(reviewAdaptor.updateReview(eq(reviewId), eq("수정된 내용"), any()))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.updateReview(reviewId, request));
	}

	@Test
	@DisplayName("상품 리뷰 목록 조회 테스트")
	void getReviewsByProduct() {
		// given
		long productId = 1L;
		Pageable pageable = PageRequest.of(0, 5);
		PageResponse<ResponseReviewPageDTO> dummyPage = new PageResponse<>();

		when(productReviewAdaptor.getReviewsByProduct(eq(productId), eq(pageable)))
			.thenReturn(ResponseEntity.ok(dummyPage));

		// when
		PageResponse<ResponseReviewPageDTO> result = reviewService.getReviewsByProduct(productId, pageable);

		// then
		assertEquals(dummyPage, result);
	}

	@Test
	@DisplayName("상품 리뷰 목록 조회 테스트 - 실패(HTTP 오류)")
	void getReviewsByProduct_Fail_ReviewProcessException() {
		// given
		long productId = 1L;
		Pageable pageable = PageRequest.of(0, 5);

		when(productReviewAdaptor.getReviewsByProduct(eq(productId), eq(pageable)))
			.thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.getReviewsByProduct(productId, pageable));
	}

	@Test
	@DisplayName("상품 리뷰 정보 조회 테스트")
	void getReviewInfo() {
		// given
		long productId = 1L;
		ResponseReviewInfoDTO response = new ResponseReviewInfoDTO(4.5, 20, List.of(1, 2, 3, 4, 5));

		when(productReviewAdaptor.getReviewInfo(productId))
			.thenReturn(ResponseEntity.ok(response));

		// when
		ResponseReviewInfoDTO result = reviewService.getReviewInfo(productId);

		// then
		assertEquals(response, result);
	}

	@Test
	@DisplayName("상품 리뷰 정보 조회 테스트 - 실패(HTTP 오류)")
	void getReviewInfo_Fail_ReviewProcessException() {
		// given
		long productId = 1L;

		when(productReviewAdaptor.getReviewInfo(productId))
			.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.getReviewInfo(productId));
	}

}