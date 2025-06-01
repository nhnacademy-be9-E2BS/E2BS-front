package com.nhnacademy.front.product.product.controller;

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
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductService;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewInfoDTO;
import com.nhnacademy.front.review.model.dto.response.ResponseReviewPageDTO;
import com.nhnacademy.front.review.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
@Slf4j
public class ProductController {

	private final ProductService productService;
	private final UserCategoryService userCategoryService;
	private final ReviewService reviewService;

	/**
	 * 사용자 - 도서 상세 페이지 조회
	 */
	@GetMapping("/{bookId}")
	public String getProduct(@PathVariable Long bookId, Model model,
		                     @PageableDefault(size = 5, sort = "reviewCreatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		// 상품 상세
		ResponseProductReadDTO response = productService.getProduct(bookId);
		model.addAttribute("product", response);

		// 리뷰 상세
		ResponseReviewInfoDTO reviewInfo = reviewService.getReviewInfo(bookId);
		PageResponse<ResponseReviewPageDTO> reviewResponse = reviewService.getReviewsByProduct(bookId, pageable);
		Page<ResponseReviewPageDTO> reviewsByProduct = PageResponseConverter.toPage(reviewResponse);

		model.addAttribute("reviewsByProduct", reviewsByProduct);
		model.addAttribute("totalGradeAvg", reviewInfo.getTotalGradeAvg());
		model.addAttribute("totalCount", reviewInfo.getTotalCount());
		model.addAttribute("starCounts", reviewInfo.getStarCounts());

		return "product/product-detail";
	}

	/**
	 * 사용자 - 카테고리를 눌러서 그 카테고리에 해당하는 도서 리스트 조회 (페이징)
	 */
	@GetMapping("/category/{categoryId}")
	public String getProduct(@PageableDefault(page = 0, size = 9) Pageable pageable,
		@PathVariable Long categoryId, Model model) {
		PageResponse<ResponseProductReadDTO> response = productService.getProductsByCategoryId(pageable, categoryId);
		Page<ResponseProductReadDTO> products = PageResponseConverter.toPage(response);

		ResponseCategoryDTO category = userCategoryService.getCategoriesById(categoryId);

		model.addAttribute("products", products);
		model.addAttribute("rootCategory", category);

		return "product/category";
	}

	/**
	 * 테스트 - 삭제 예정
	 */
	@GetMapping("/test/{bookId}")
	public String getProductTest(@PathVariable Long bookId, Model model) {
		ResponseProductReadDTO response = productService.getProduct(bookId);

		model.addAttribute("product", response);
		return "product/product-detail-test";
	}

	/**
	 * 테스트 - 삭제 예정
	 */
	@GetMapping("/test2/{bookId}")
	public String getProductTest2(@PathVariable Long bookId, Model model) {
		ResponseProductReadDTO response = productService.getProduct(bookId);

		model.addAttribute("product", response);
		return "product/product-detail";
	}
}
