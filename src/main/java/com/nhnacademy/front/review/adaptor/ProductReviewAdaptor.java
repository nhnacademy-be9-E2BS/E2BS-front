package com.nhnacademy.front.review.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewInfoDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;

@FeignClient(name = "gateway-service", contextId = "product-review-adaptor")
public interface ProductReviewAdaptor {

	/**
	 * 상품 리뷰 페이징 목록 조회
	 */
	@GetMapping("/api/products/{productId}/reviews")
	ResponseEntity<PageResponse<ResponseReviewPageDTO>> getReviewsByProduct(@PathVariable long productId, Pageable pageable);

	/**
	 * 상품 리뷰의 전체 평점 및 각 등급의 리뷰 개수 정보 조회
	 */
	@GetMapping("/api/products/{productId}/reviews/info")
	ResponseEntity<ResponseReviewInfoDTO> getReviewInfo(@PathVariable long productId);

}
