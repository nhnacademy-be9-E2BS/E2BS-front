package com.nhnacademy.front.review.controller;

import java.util.Objects;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.review.model.dto.request.RequestCreateReviewDTO;
import com.nhnacademy.front.review.model.dto.request.RequestUpdateReviewDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewInfoDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;
import com.nhnacademy.front.review.service.ReviewService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;


	@GetMapping("/products/{productId}/reviews/register")
	public String createReviewForm() {
		return "review/review-register";
	}

	@PostMapping("/reviews")
	public ResponseEntity<Void> createReview(@Validated @ModelAttribute RequestCreateReviewDTO  requestDto, BindingResult bindingResult,
								HttpServletRequest request) {
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

	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> updateReview(@PathVariable long reviewId, @Validated @RequestBody RequestUpdateReviewDTO requestDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		reviewService.updateReview(reviewId, requestDto);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/customers/reviews")
	public String getReviewsByCustomer(@PageableDefault(size = 5) Pageable pageable, Model model) {
		// PageResponse<ResponseReviewPageDTO> reviewsByCustomer = reviewService.getReviewsByCustomer(customerId, pageable);
		//
		// model.addAttribute("reviewsByCustomer", reviewsByCustomer);
		return "review/customer-review";
	}

	@GetMapping("/products/{productId}/reviews")
	public String getReviewsByProduct(@PathVariable long productId, @PageableDefault(size = 5, sort = "reviewCreatedAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
		ResponseReviewInfoDTO reviewInfo = reviewService.getReviewInfo(productId);

		PageResponse<ResponseReviewPageDTO> response = reviewService.getReviewsByProduct(productId, pageable);
		Page<ResponseReviewPageDTO> reviewsByProduct = PageResponseConverter.toPage(response);

		model.addAttribute("reviewsByProduct", reviewsByProduct);
		model.addAttribute("totalGradeAvg", reviewInfo.getTotalGradeAvg());
		model.addAttribute("totalCount", reviewInfo.getTotalCount());
		model.addAttribute("starCounts", reviewInfo.getStarCounts());

		return "review/product-detail";
	}

}
