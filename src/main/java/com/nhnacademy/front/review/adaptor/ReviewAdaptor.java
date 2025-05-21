package com.nhnacademy.front.review.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewDTO;
import com.nhnacademy.front.review.model.dto.request.RequestUpdateReviewDTO;

@FeignClient(name = "review-adaptor", url = "${review.url}")
public interface ReviewAdaptor {

	/**
	 * 리뷰 생성
	 */
	@PostMapping
	ResponseEntity<Void> createReview(@RequestBody RequestCreateReviewDTO request);

	/**
	 * 리뷰 수정
	 */
	@PutMapping("/{reviewId}")
	ResponseEntity<Void> updateReview(@PathVariable long reviewId, @RequestBody RequestUpdateReviewDTO request);

}