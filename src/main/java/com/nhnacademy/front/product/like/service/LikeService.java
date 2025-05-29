package com.nhnacademy.front.product.like.service;

import org.springframework.data.domain.Pageable;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.like.model.dto.request.RequestCreateLikeDTO;
import com.nhnacademy.front.product.like.model.dto.response.ResponseLikedProductDTO;

public interface LikeService {
	void createLike(long productId, RequestCreateLikeDTO requestDto);
	void deleteLike(long productId, String memberId);
	PageResponse<ResponseLikedProductDTO> getLikeProductsByCustomer(String memberId, Pageable pageable);
	Long getLikeCount(long productId);
}
