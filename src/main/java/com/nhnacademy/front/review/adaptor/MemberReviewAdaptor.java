package com.nhnacademy.front.review.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.review.model.dto.response.ResponseMemberReviewDTO;

@FeignClient(name = "member-review-adaptor", url = "${member.state.url}")
public interface MemberReviewAdaptor {

	/**
	 * 회원 리뷰 페이징 목록 조회
	 */
	@GetMapping("/{memberId}/reviews")
	ResponseEntity<PageResponse<ResponseMemberReviewDTO>> getReviewsByMember(@PathVariable String memberId, Pageable pageable);

}
