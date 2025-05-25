package com.nhnacademy.front.review.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
import com.nhnacademy.front.review.service.ReviewService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ProductReviewAdaptor productReviewAdaptor;
	private final ReviewAdaptor reviewAdaptor;


	@Override
	public void createReview(RequestCreateReviewDTO request) throws FeignException {
		RequestCreateReviewMetaDTO requestMeta = new RequestCreateReviewMetaDTO(request.getProductId(), request.getCustomerId(), request.getMemberId(), request.getReviewContent(), request.getReviewGrade());
		ResponseEntity<Void> result = reviewAdaptor.createReview(requestMeta, request.getReviewImage());

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new ReviewProcessException("리뷰 생성 실패: " + result.getStatusCode());
		}
	}

	@Override
	public ResponseUpdateReviewDTO updateReview(long reviewId, RequestUpdateReviewDTO request) throws FeignException {
		ResponseEntity<ResponseUpdateReviewDTO> result = reviewAdaptor.updateReview(reviewId, request.getReviewContent(), request.getReviewImage());

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new ReviewProcessException("리뷰 수정 실패: " + result.getStatusCode());
		}
		return result.getBody();
	}

	@Override
	public PageResponse<ResponseReviewPageDTO> getReviewsByProduct(long productId, Pageable pageable) throws FeignException {
		ResponseEntity<PageResponse<ResponseReviewPageDTO>> result = productReviewAdaptor.getReviewsByProduct(productId, pageable);

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new ReviewProcessException("상품 리뷰 페이징 목록 조회 실패: " + result.getStatusCode());
		}
		return result.getBody();
	}

	@Override
	public ResponseReviewInfoDTO getReviewInfo(long productId) throws FeignException{
		ResponseEntity<ResponseReviewInfoDTO> result = productReviewAdaptor.getReviewInfo(productId);

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new ReviewProcessException("상품 리뷰 정보 조회 실패: " + result.getStatusCode());
		}
		return result.getBody();
	}

}
