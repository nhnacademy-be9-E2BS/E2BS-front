package com.nhnacademy.front.product.like.controller;

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
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.product.like.model.dto.request.RequestCreateLikeDTO;
import com.nhnacademy.front.product.like.model.dto.response.ResponseLikedProductDTO;
import com.nhnacademy.front.product.like.service.LikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "Like", description = "상품 좋아요 관련 API")
@Controller
@RequiredArgsConstructor
public class LikeController {

	private final LikeService likeService;


	@JwtTokenCheck
	@Operation(summary = "좋아요 등록", description = "회원이 상품에 대한 좋아요를 등록합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "좋아요 등록 성공"),
		@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
		@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@PostMapping("/products/{productId}/likes")
	public ResponseEntity<Void> addLike(@Parameter(description = "좋아요를 등록할 상품 ID", example = "123", required = true) @PathVariable long productId,
		                                @Parameter(hidden = true) HttpServletRequest request) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		RequestCreateLikeDTO requestDto = new RequestCreateLikeDTO();
		requestDto.setMemberId(memberId);

		likeService.createLike(productId, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@JwtTokenCheck
	@Operation(summary = "좋아요 취소", description = "회원이 상품에 대한 좋아요를 취소합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
		@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
		@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@DeleteMapping("/products/{productId}/likes")
	public ResponseEntity<Void> deleteLike(@Parameter(description = "좋아요를 취소할 상품 ID", example = "123", required = true) @PathVariable long productId,
		                                   @Parameter(hidden = true) HttpServletRequest request) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		likeService.deleteLike(productId, memberId);
		return ResponseEntity.ok().build();
	}

	@JwtTokenCheck
	@Operation(summary = "좋아요 상품 목록 조회", description = "회원이 좋아요한 상품 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@GetMapping("/products/likes")
	public String getLikedProductsByCustomer(@Parameter(hidden = true) @PageableDefault(size = 6, sort = "likeCreatedAt", direction = Sort.Direction.DESC) Pageable pageable,
		                                     @Parameter(hidden = true) HttpServletRequest request,
		                                     @Parameter(hidden = true) Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		PageResponse<ResponseLikedProductDTO> response = likeService.getLikeProductsByCustomer(memberId, pageable);
		Page<ResponseLikedProductDTO> likeProductsByCustomer = PageResponseConverter.toPage(response);

		model.addAttribute("likeProducts", likeProductsByCustomer);

		return "member/mypage/products-like";
	}

}
