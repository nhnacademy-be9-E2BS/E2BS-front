package com.nhnacademy.front.product.like.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.product.like.model.dto.request.RequestCreateLikeDTO;
import com.nhnacademy.front.product.like.model.dto.response.ResponseLikedProductDTO;

@FeignClient(name = "gateway-service", contextId = "like-adaptor")
public interface LikeAdaptor {

	@PostMapping("/api/products/{productId}/likes")
	ResponseEntity<Void> createLike(@PathVariable long productId, @RequestBody RequestCreateLikeDTO likeDTO);

	@DeleteMapping("/api/products/{productId}/likes")
	ResponseEntity<Void> deleteLike(@PathVariable long productId, @RequestParam String memberId);

	@GetMapping("/api/products/likes")
	ResponseEntity<PageResponse<ResponseLikedProductDTO>> getLikedProductsByCustomer(@RequestParam String memberId, Pageable pageable);

	@GetMapping("/api/products/{productId}/likes/counts")
	ResponseEntity<Long> getLikeCounts(@PathVariable long productId);

}
