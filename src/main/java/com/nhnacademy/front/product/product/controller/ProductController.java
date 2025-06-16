package com.nhnacademy.front.product.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.jwt.parser.JwtHasToken;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductService;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewInfoDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;
import com.nhnacademy.front.review.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "도서(사용자)", description = "사용자 도서 관련 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class ProductController {

	private final ProductService productService;
	private final ReviewService reviewService;
	private final DeliveryFeeSevice deliveryFeeSevice;

	/**
	 * 사용자 - 도서 상세 페이지 조회
	 */
	@Operation(summary = "도서 단일 조회",
		description = "도서 상세페이지를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공")
		})
	@GetMapping("/{book-id}")
	public String getProduct(
		@Parameter(description = "조회할 도서 ID", example = "1", required = true) @PathVariable("book-id") Long bookId,
		Model model,
		@Parameter(description = "페이징 정보") @PageableDefault(size = 5, sort = "reviewCreatedAt", direction = Sort.Direction.DESC) Pageable pageable,
		@Parameter(hidden = true) HttpServletRequest request) {
		String memberId = "";
		if (JwtHasToken.hasToken(request)) {
			memberId = JwtGetMemberId.jwtGetMemberId(request);
		}

		// 상품 상세
		ResponseProductReadDTO response = productService.getProduct(bookId, memberId);
		model.addAttribute("product", response);

		// 리뷰 상세
		ResponseReviewInfoDTO reviewInfo = reviewService.getReviewInfo(bookId);
		PageResponse<ResponseReviewPageDTO> reviewResponse = reviewService.getReviewsByProduct(bookId, pageable);
		Page<ResponseReviewPageDTO> reviewsByProduct = PageResponseConverter.toPage(reviewResponse);

		// 현재 적용 중인 배송비 정책
		ResponseDeliveryFeeDTO deliveryFee = deliveryFeeSevice.getCurrentDeliveryFee();

		// 적용 중인 할인률 계산
		long discountRate = (long)(((double)(response.getProductRegularPrice() - response.getProductSalePrice())
			/ response.getProductRegularPrice()) * 100);

		List<ResponseProductReadDTO> recommendedProducts = productService.getRecommendedProducts(bookId, memberId);

		model.addAttribute("reviewsByProduct", reviewsByProduct);
		model.addAttribute("totalGradeAvg", reviewInfo.getTotalGradeAvg());
		model.addAttribute("totalCount", reviewInfo.getTotalCount());
		model.addAttribute("starCounts", reviewInfo.getStarCounts());
		model.addAttribute("deliveryFee", deliveryFee);
		model.addAttribute("discountRate", discountRate);
		model.addAttribute("recommendedProducts", recommendedProducts);

		return "product/product-detail";
	}
}
