package com.nhnacademy.front.review.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.front.common.config.FeignFormDataSupportConfig;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewDTO;
import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewMetaDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseUpdateReviewDTO;

@FeignClient(name = "gateway-service", contextId = "review-adaptor", configuration = FeignFormDataSupportConfig.class)
public interface ReviewAdaptor {

	/**
	 * 리뷰 생성
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/api/reviews")
	ResponseEntity<Void> createReview(@RequestPart("requestMeta") RequestCreateReviewMetaDTO reviewMeta,
		@RequestPart("reviewImage") MultipartFile reviewImage);

	/**
	 * 리뷰 수정
	 */
	@PutMapping(path = "/api/reviews/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<ResponseUpdateReviewDTO> updateReview(@PathVariable long reviewId,
		@RequestPart("reviewContent") String reviewContent, @RequestPart("reviewImage") MultipartFile reviewImage);

	/**
	 * 주문 상세 Id로 리뷰 정보 가져오기
	 */
	@GetMapping("/api/reviews/{orderDetailId}")
	ResponseEntity<ResponseReviewDTO> findReviewByOrderDetailId(@PathVariable long orderDetailId);

}