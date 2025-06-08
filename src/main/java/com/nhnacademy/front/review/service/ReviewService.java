package com.nhnacademy.front.review.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewDTO;
import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewDTO;
import com.nhnacademy.front.review.model.dto.request.RequestUpdateReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseMemberReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewInfoDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseUpdateReviewDTO;

public interface ReviewService {
	void createReview(RequestCreateReviewDTO request);
	ResponseUpdateReviewDTO updateReview(long reviewId, RequestUpdateReviewDTO request);
	PageResponse<ResponseReviewPageDTO> getReviewsByProduct(long productId, Pageable pageable);
	ResponseReviewInfoDTO getReviewInfo(long productId);
	PageResponse<ResponseMemberReviewDTO> getReviewsByMember(String memberId, Pageable pageable);
	Boolean isReviewedByOrder(String orderCode);
	ResponseReviewDTO findReviewByOrderDetailId(long orderDetailId);
}
