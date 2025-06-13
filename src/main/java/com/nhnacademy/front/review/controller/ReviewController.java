package com.nhnacademy.front.review.controller;

import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewDTO;
import com.nhnacademy.front.review.model.dto.request.RequestUpdateReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseMemberReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseUpdateReviewDTO;
import com.nhnacademy.front.review.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "Review", description = "리뷰 관련 API")
@Controller
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;


	@Operation(summary = "리뷰 작성",
		description = "주문한 회원 또는 비회원이 리뷰를 작성합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "리뷰 작성 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
		})
	@PostMapping("/reviews")
	public ResponseEntity<Void> createReview(@Parameter(description = "리뷰 작성 요청 DTO", required = true) @Validated @ModelAttribute RequestCreateReviewDTO requestDto,
		                                     @Parameter(hidden = true) BindingResult bindingResult,
		                                     @Parameter(hidden = true) HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		if (Objects.isNull(request.getSession().getAttribute("guestId"))) {
			String memberId = JwtGetMemberId.jwtGetMemberId(request);
			requestDto.setMemberId(memberId);
		}

		reviewService.createReview(requestDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "리뷰 수정",
		description = "기존 리뷰를 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "리뷰 수정 성공", content = @Content(schema = @Schema(implementation = ResponseUpdateReviewDTO.class))),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
		})
	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<ResponseUpdateReviewDTO> updateReview(@Parameter(description = "리뷰 ID", required = true) @PathVariable long reviewId,
		                                                        @Parameter(description = "리뷰 수정 요청 DTO", required = true) @ModelAttribute RequestUpdateReviewDTO requestDto) {
		ResponseUpdateReviewDTO body = reviewService.updateReview(reviewId, requestDto);
		return ResponseEntity.ok(body);
	}

	@JwtTokenCheck
	@Operation(summary = "회원 리뷰 목록 조회", description = "마이페이지에서 회원이 작성한 리뷰 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "회원 리뷰 목록 조회 성공")
	@GetMapping("/mypage/reviews")
	public String getReviewsByCustomer(@Parameter(hidden = true) HttpServletRequest request,
		                               @Parameter(hidden = true) Model model,
		                               @Parameter(hidden = true) @PageableDefault(size = 5, sort = "reviewCreatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		PageResponse<ResponseMemberReviewDTO> response = reviewService.getReviewsByMember(memberId, pageable);
		model.addAttribute("reviewsByMember", PageResponseConverter.toPage(response));

		return "member/mypage/reviews";
	}

	@Operation(summary = "주문 상세 ID로 리뷰 조회",
		description = "주문 상세 ID로 리뷰 정보를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "주문 상세 ID로 리뷰 조회 성공", content = @Content(schema = @Schema(implementation = ResponseReviewDTO.class))),
			@ApiResponse(responseCode = "404", description = "해당 리뷰가 존재하지 않음")
		})
	@GetMapping("/reviews/{orderDetailId}")
	public ResponseEntity<ResponseReviewDTO> getReviewByOrderDetailId(@Parameter(description = "주문 상세 ID", required = true) @PathVariable long orderDetailId) {
		ResponseReviewDTO body = reviewService.findReviewByOrderDetailId(orderDetailId);
		return ResponseEntity.ok(body);
	}

}
