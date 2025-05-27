package com.nhnacademy.front.product.like.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.product.like.model.dto.request.RequestCreateLikeDTO;
import com.nhnacademy.front.product.like.model.dto.response.ResponseLikedProductDTO;

@FeignClient(name = "like-adaptor", url = "${product.url}")
public interface LikeAdaptor {

	@PostMapping("/{productId}/likes")
	ResponseEntity<Void> createLike(@PathVariable long productId, @RequestBody RequestCreateLikeDTO likeDTO);

	@DeleteMapping("/{productId}/likes")
	ResponseEntity<Void> deleteLike(@PathVariable long productId, @RequestParam String memberId);

	@GetMapping("/likes")
	ResponseEntity<Page<ResponseLikedProductDTO>> getLikedProductsByCustomer(@RequestParam String memberId, Pageable pageable);

	@GetMapping("/{productId}/likes/counts")
	ResponseEntity<Long> getLikeCounts(@PathVariable long productId);

}
