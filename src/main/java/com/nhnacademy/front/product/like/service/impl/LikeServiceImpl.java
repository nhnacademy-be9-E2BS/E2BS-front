package com.nhnacademy.front.product.like.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.like.adaptor.LikeAdaptor;
import com.nhnacademy.front.product.like.exception.LikeProcessException;
import com.nhnacademy.front.product.like.model.dto.request.RequestCreateLikeDTO;
import com.nhnacademy.front.product.like.model.dto.response.ResponseLikedProductDTO;
import com.nhnacademy.front.product.like.service.LikeService;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

	private final LikeAdaptor likeAdaptor;


	@Override
	public void createLike(long productId, RequestCreateLikeDTO requestDto) {
		ResponseEntity<Void> result = likeAdaptor.createLike(productId, requestDto);
		
		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new LikeProcessException("좋아요 생성 실패: " + result.getStatusCode());
		}
	}

	@Override
	public void deleteLike(long productId, String memberId) {
		ResponseEntity<Void> result = likeAdaptor.deleteLike(productId, memberId);

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new LikeProcessException("좋아요 삭제 실패: " + result.getStatusCode());
		}
	}

	@Override
	public PageResponse<ResponseLikedProductDTO> getLikeProductsByCustomer(String memberId, Pageable pageable) {
		ResponseEntity<PageResponse<ResponseLikedProductDTO>> result = likeAdaptor.getLikedProductsByCustomer(memberId, pageable);

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new LikeProcessException("좋아요 상품 페이징 목록 조회 실패: " + result.getStatusCode());
		}
		return result.getBody();
	}

	@Override
	public Long getLikeCount(long productId) {
		ResponseEntity<Long> result = likeAdaptor.getLikeCounts(productId);

		if (!result.getStatusCode().is2xxSuccessful()) {
			throw new LikeProcessException("좋아요 상품 페이징 목록 조회 실패: " + result.getStatusCode());
		}
		return result.getBody();
	}

}
