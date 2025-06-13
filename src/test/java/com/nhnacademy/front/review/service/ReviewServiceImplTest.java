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
import com.nhnacademy.front.order.order.adaptor.OrderAdaptor;
import com.nhnacademy.front.review.adaptor.MemberReviewAdaptor;
import com.nhnacademy.front.review.adaptor.ProductReviewAdaptor;
import com.nhnacademy.front.review.adaptor.ReviewAdaptor;
import com.nhnacademy.front.review.exception.ReviewProcessException;
import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewDTO;
import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewMetaDTO;
import com.nhnacademy.front.review.model.dto.request.RequestUpdateReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseMemberReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewInfoDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseUpdateReviewDTO;
import com.nhnacademy.front.review.service.impl.ReviewServiceImpl;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

	@Mock
	private MemberReviewAdaptor memberReviewAdaptor;

	@Mock
	private ProductReviewAdaptor productReviewAdaptor;

	@Mock
	private ReviewAdaptor reviewAdaptor;

	@Mock
	private OrderAdaptor orderAdaptor;

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
	@DisplayName("리뷰 생성 테스트 - 실패(HTTP 응답 실패)")
	void createReview_Fail_StatusNot2xx() {
		// given
		RequestCreateReviewMetaDTO meta = new RequestCreateReviewMetaDTO(1L, null, "memberId", "좋아요", 5);
		MockMultipartFile mockImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", "data".getBytes());
		RequestCreateReviewDTO request = new RequestCreateReviewDTO(1L, null, "memberId", "좋아요", 5, mockImage);

		when(reviewAdaptor.createReview(meta, mockImage))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.createReview(request));
	}

	@Test
	@DisplayName("리뷰 생성 테스트 - 실패(FeignException)")
	void createReview_Fail_FeignException() {
		// given
		RequestCreateReviewMetaDTO meta = new RequestCreateReviewMetaDTO(1L, null, "memberId", "좋아요", 5);
		MockMultipartFile mockImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", "data".getBytes());
		RequestCreateReviewDTO request = new RequestCreateReviewDTO(1L, null, "memberId", "좋아요", 5, mockImage);

		when(reviewAdaptor.createReview(meta, mockImage))
			.thenThrow(mock(FeignException.class));

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
	@DisplayName("리뷰 수정 테스트 - 실패(HTTP 응답 실패)")
	void updateReview_Fail_StatusNot2xx() {
		// given
		long reviewId = 1L;
		RequestUpdateReviewDTO request = new RequestUpdateReviewDTO("수정된 내용", null);

		when(reviewAdaptor.updateReview(eq(reviewId), eq("수정된 내용"), any()))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.updateReview(reviewId, request));
	}

	@Test
	@DisplayName("리뷰 수정 테스트 - 실패(FeignException)")
	void updateReview_Fail_FeignException() {
		// given
		long reviewId = 1L;
		RequestUpdateReviewDTO request = new RequestUpdateReviewDTO("내용", null);

		when(reviewAdaptor.updateReview(eq(reviewId), eq("내용"), any()))
			.thenThrow(mock(FeignException.class));

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

		when(productReviewAdaptor.getReviewsByProduct(anyLong(), any(PageRequest.class)))
			.thenReturn(ResponseEntity.ok(dummyPage));

		// when
		PageResponse<ResponseReviewPageDTO> result = reviewService.getReviewsByProduct(productId, pageable);

		// then
		assertEquals(dummyPage, result);
	}

	@Test
	@DisplayName("상품 리뷰 목록 조회 테스트 - 실패(HTTP 응답 실패)")
	void getReviewsByProduct_Fail_StatusNot2xx() {
		// given
		long productId = 1L;
		Pageable pageable = PageRequest.of(0, 5);

		when(productReviewAdaptor.getReviewsByProduct(anyLong(), any(PageRequest.class)))
			.thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.getReviewsByProduct(productId, pageable));
	}

	@Test
	@DisplayName("상품 리뷰 목록 조회 테스트 - 실패(FeignException)")
	void getReviewsByProduct_Fail_FeignException() {
		// given
		long productId = 1L;
		Pageable pageable = PageRequest.of(0, 5);

		when(productReviewAdaptor.getReviewsByProduct(productId, pageable))
			.thenThrow(mock(FeignException.class));

		// when & then
		assertThrows(ReviewProcessException.class, () -> {
			// 예외를 던질 수 있는 하나의 호출만 람다 내에 작성
			reviewService.getReviewsByMember(any(), any());
		});
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
	@DisplayName("상품 리뷰 정보 조회 테스트 - 실패(HTTP 응답 실패)")
	void getReviewInfo_Fail_StatusNot2xx() {
		// given
		long productId = 1L;

		when(productReviewAdaptor.getReviewInfo(productId))
			.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.getReviewInfo(productId));
	}

	@Test
	@DisplayName("상품 리뷰 정보 조회 테스트 - 실패(FeignException)")
	void getReviewInfo_Fail_FeignException() {
		// given
		long productId = 1L;

		when(productReviewAdaptor.getReviewInfo(productId))
			.thenThrow(mock(FeignException.class));

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.getReviewInfo(productId));
	}

	@Test
	@DisplayName("회원 리뷰 목록 조회 테스트")
	void getReviewsByMember() {
		// given
		PageResponse<ResponseMemberReviewDTO> page = new PageResponse<>();
		when(memberReviewAdaptor.getReviewsByMember(any(), any())).thenReturn(ResponseEntity.ok(page));

		// when
		PageResponse<ResponseMemberReviewDTO> result = reviewService.getReviewsByMember("mem1", Pageable.ofSize(5));

		// then
		assertEquals(result, page);
	}

	@Test
	@DisplayName("회원 리뷰 목록 조회 테스트 - 실패(HTTP 오류)")
	void getReviewsByMember_Fail_ReviewProcessException() {
		// given
		when(memberReviewAdaptor.getReviewsByMember(any(), any()))
			.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

		// when & then
		assertThrows(ReviewProcessException.class, () -> {
			reviewService.getReviewsByMember(any(), any());
		});
	}

	@Test
	@DisplayName("리뷰 여부 조회 테스트")
	void isReviewedByOrder() {
		// given
		when(orderAdaptor.isReviewedByOrder(any())).thenReturn(ResponseEntity.ok(true));

		// when
		Boolean result = reviewService.isReviewedByOrder("orderCode");

		// then
		assertTrue(result);
	}

	@Test
	@DisplayName("리뷰 여부 조회 테스트 - 실패(HTTP 응답 실패)")
	void isReviewedByOrder_Fail_StatusNot2xx() {
		// given
		when(orderAdaptor.isReviewedByOrder(any()))
			.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

		// when & then
		assertThrows(ReviewProcessException.class, () -> {
			reviewService.isReviewedByOrder(any());
		});
	}

	@Test
	@DisplayName("리뷰 여부 조회 테스트 - 실패(FeignException)")
	void isReviewedByOrder_Fail_FeignException() {
		// given
		when(orderAdaptor.isReviewedByOrder(any()))
			.thenThrow(mock(FeignException.class));

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.isReviewedByOrder("orderCode"));
	}

	@Test
	@DisplayName("주문 상세 ID로 리뷰 조회 테스트")
	void findReviewByOrderDetailId() {
		// given
		ResponseReviewDTO dto = new ResponseReviewDTO();
		when(reviewAdaptor.findReviewByOrderDetailId(anyLong())).thenReturn(ResponseEntity.ok(dto));

		// when
		ResponseReviewDTO result = reviewService.findReviewByOrderDetailId(1L);

		// then
		assertEquals(result, dto);
	}

	@Test
	@DisplayName("주문 상세 ID로 리뷰 조회 테스트 - 실패(HTTP 응답 실패)")
	void findReviewByOrderDetailId_Fail_StatusNot2xx() {
		// given
		when(reviewAdaptor.findReviewByOrderDetailId(anyLong()))
			.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

		// when & then
		assertThrows(ReviewProcessException.class, () ->  {
			reviewService.findReviewByOrderDetailId(anyLong());
		});
	}

	@Test
	@DisplayName("주문 상세 ID로 리뷰 조회 테스트 - 실패(FeignException)")
	void findReviewByOrderDetailId_Fail_FeignException() {
		// given
		when(reviewAdaptor.findReviewByOrderDetailId(anyLong()))
			.thenThrow(mock(FeignException.class));

		// when & then
		assertThrows(ReviewProcessException.class, () -> reviewService.findReviewByOrderDetailId(1L));
	}
	
}