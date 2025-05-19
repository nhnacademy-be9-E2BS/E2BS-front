package com.nhnacademy.front.review.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;

@FeignClient(name = "customer-review-adaptor", url = "${customer.url}")
public interface CustomerReviewAdaptor {

	/**
	 * 고객 리뷰 페이징 목록 조회
	 */
	@GetMapping("/{customerId}/reviews")
	ResponseEntity<PageResponse<ResponseReviewPageDTO>> getReviewsByCustomer(@PathVariable long customerId, Pageable pageable);

}
