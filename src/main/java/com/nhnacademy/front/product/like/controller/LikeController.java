package com.nhnacademy.front.product.like.controller;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.product.like.model.dto.request.RequestCreateLikeDTO;
import com.nhnacademy.front.product.like.model.dto.response.ResponseLikedProductDTO;
import com.nhnacademy.front.product.like.service.LikeService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LikeController {

	private final LikeService likeService;


	@JwtTokenCheck
	@PostMapping("/products/{productId}/likes")
	public ResponseEntity<Void> addLike(@PathVariable Long productId, HttpServletRequest request) {
		if (Objects.isNull(productId)) {
			throw new IllegalArgumentException();
		}

		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		RequestCreateLikeDTO requestDto = new RequestCreateLikeDTO();
		requestDto.setMemberId(memberId);

		likeService.createLike(productId, requestDto);
		return  ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@JwtTokenCheck
	@DeleteMapping("/products/{productId}/likes")
	public ResponseEntity<Void> deleteLike(@PathVariable Long productId, HttpServletRequest request) {
		if (Objects.isNull(productId)) {
			throw new IllegalArgumentException();
		}

		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		likeService.deleteLike(productId, memberId);
		return ResponseEntity.ok().build();
	}

	@JwtTokenCheck
	@GetMapping("/products/likes")
	public String getLikedProductsByCustomer(@PageableDefault(size = 6, sort = "likeCreatedAt", direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request, Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		PageResponse<ResponseLikedProductDTO> response = likeService.getLikeProductsByCustomer(memberId, pageable);
		Page<ResponseLikedProductDTO> likeProductsByCustomer = PageResponseConverter.toPage(response);

		model.addAttribute("likeProducts", likeProductsByCustomer);

		return "product/products-like";
	}

}
