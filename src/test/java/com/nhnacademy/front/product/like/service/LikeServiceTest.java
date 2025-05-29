package com.nhnacademy.front.product.like.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.like.adaptor.LikeAdaptor;
import com.nhnacademy.front.product.like.exception.LikeProcessException;
import com.nhnacademy.front.product.like.model.dto.request.RequestCreateLikeDTO;
import com.nhnacademy.front.product.like.model.dto.response.ResponseLikedProductDTO;
import com.nhnacademy.front.product.like.service.impl.LikeServiceImpl;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

	@Mock
	private LikeAdaptor likeAdaptor;

	@InjectMocks
	private LikeServiceImpl likeService;

	private String memberId;
	private long productId;

	@BeforeEach
	void setUp() {
		memberId = "testMember";
		productId = 1L;
	}

	@Test
	@DisplayName("좋아요 생성")
	void createLike() {
		// given
		RequestCreateLikeDTO requestCreateLikeDTO = new RequestCreateLikeDTO(memberId);

		when(likeAdaptor.createLike(anyLong(), any(RequestCreateLikeDTO.class)))
			.thenReturn(ResponseEntity.ok().build());

		// when
		likeService.createLike(productId, requestCreateLikeDTO);

		// then
		verify(likeAdaptor, times(1)).createLike(productId, requestCreateLikeDTO);
	}

	@Test
	@DisplayName("좋아요 생성 - 실패(HTTP 오류)")
	void createLike_Fail_LikeProcessException() {
		// given
		RequestCreateLikeDTO requestCreateLikeDTO = new RequestCreateLikeDTO(memberId);

		when(likeAdaptor.createLike(anyLong(), any(RequestCreateLikeDTO.class)))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		// when & then
		assertThrows(LikeProcessException.class, () -> likeService.createLike(productId, requestCreateLikeDTO));

	}

	@Test
	@DisplayName("좋아요 삭제")
	void deleteLike() {
		// given
		when(likeAdaptor.deleteLike(anyLong(), anyString()))
			.thenReturn(ResponseEntity.ok().build());

		// when
		likeService.deleteLike(productId, memberId);

		// then
		verify(likeAdaptor, times(1)).deleteLike(productId, memberId);
	}

	@Test
	@DisplayName("좋아요 삭제 - 실패(HTTP 오류)")
	void deleteLike_Fail_LikeProcessException() {
		// given
		when(likeAdaptor.deleteLike(anyLong(), anyString()))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		// when & then
		assertThrows(LikeProcessException.class, () -> likeService.deleteLike(productId, memberId));
	}

	@Test
	@DisplayName("회원의 좋아요 상품 페이징 목록 조회")
	void getLikeProductsByCustomer() {
		// given
		Pageable pageable = PageRequest.of(0, 6);

		List<ResponseLikedProductDTO> likedProductsList = List.of(
			new ResponseLikedProductDTO(1L, "Product A", 20000, "출판사 A", "http://localhost:9000/aaa.jpg", 5, 4.5, 3, LocalDateTime.now()),
			new ResponseLikedProductDTO(2L, "Product B", 25000, "출판사 B", "http://localhost:9000/bbb.jpg", 10, 4.7, 5, LocalDateTime.now())
		);

		PageResponse.SortInfo sortInfo = new PageResponse.SortInfo();
		PageResponse.PageableInfo pageableInfo = new PageResponse.PageableInfo();
		PageResponse<ResponseLikedProductDTO> pageResponse =
			new PageResponse<>(likedProductsList, pageableInfo, true,
				2, 1, 6, 0, sortInfo, true, 2, false);


		when(likeAdaptor.getLikedProductsByCustomer(anyString(), any(Pageable.class)))
			.thenReturn(ResponseEntity.ok(pageResponse));

		// when
		PageResponse<ResponseLikedProductDTO> result = likeService.getLikeProductsByCustomer(memberId, pageable);

		// then
		assertEquals(2, result.getTotalElements());
		verify(likeAdaptor, times(1)).getLikedProductsByCustomer(memberId, pageable);
	}

	@Test
	@DisplayName("회원의 좋아요 상품 페이징 목록 조회 - 실패(HTTP 오류)")
	void getLikeProductsByCustomer_Fail_LikeProcessException() {
		// given
		Pageable pageable = PageRequest.of(0, 6);

		when(likeAdaptor.getLikedProductsByCustomer(anyString(), any(Pageable.class)))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		// when & then
		assertThrows(LikeProcessException.class, () -> likeService.getLikeProductsByCustomer(memberId, pageable));
	}

	@Test
	@DisplayName("상품 좋아요 개수 조회")
	void getLikeCount() {
		// given
		long likeCount = 5L;

		when(likeAdaptor.getLikeCounts(anyLong()))
			.thenReturn(ResponseEntity.ok(likeCount));

		// when
		Long result = likeService.getLikeCount(productId);

		// then
		assertEquals(likeCount, result);
		verify(likeAdaptor, times(1)).getLikeCounts(anyLong());
	}

	@Test
	@DisplayName("상품 좋아요 개수 조회 - 실패(HTTP 오류)")
	void getLikeCount_Fail_LikeProcessException() {
		// given
		when(likeAdaptor.getLikeCounts(anyLong()))
			.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

		// when & then
		assertThrows(LikeProcessException.class, () -> likeService.getLikeCount(productId));
	}

}
