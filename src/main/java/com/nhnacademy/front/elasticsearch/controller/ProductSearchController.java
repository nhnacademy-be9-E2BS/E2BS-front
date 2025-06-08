package com.nhnacademy.front.elasticsearch.controller;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.elasticsearch.model.dto.domain.ProductSortType;
import com.nhnacademy.front.elasticsearch.service.ProductSearchService;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books/search")
public class ProductSearchController {

	private final ProductSearchService productSearchService;
	private final UserCategoryService userCategoryService;

	/**
	 * 사용자 - 검색창에 검색어를 입력하여 도서 리스트 조회 (페이징)
	 * 정렬 기준 선택 가능 (default = 출판일자 desc)
	 */
	@GetMapping
	public String getProductsBySearch(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model,
		@RequestParam String keyword, @RequestParam(required = false) ProductSortType sort) {
		PageResponse<ResponseProductReadDTO> response = productSearchService.getProductsBySearch(pageable, keyword, sort);
		Page<ResponseProductReadDTO> products = PageResponseConverter.toPage(response);

		model.addAttribute("products", products);
		model.addAttribute("keyword", keyword);
		if(Objects.isNull(sort)) {
			model.addAttribute("sort", ProductSortType.NO_SORT.toString());
		} else {
			model.addAttribute("sort", sort.toString());
		}

		return "product/search";
	}

	/**
	 * 사용자 - 카테고리를 눌러서 그 카테고리에 해당하는 도서 리스트 조회 (페이징)
	 * 정렬 기준 선택 가능 (default = 출판일자 desc)
	 */
	@GetMapping("/category/{categoryId}")
	public String getProduct(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model,
		@PathVariable Long categoryId, @RequestParam(required = false) ProductSortType sort) {
		PageResponse<ResponseProductReadDTO> response = productSearchService.getProductsByCategory(pageable, categoryId, sort);
		Page<ResponseProductReadDTO> products = PageResponseConverter.toPage(response);

		ResponseCategoryDTO category = userCategoryService.getCategoriesById(categoryId);

		model.addAttribute("products", products);
		model.addAttribute("rootCategory", category);
		if(Objects.isNull(sort)) {
			model.addAttribute("sort", ProductSortType.NO_SORT.toString());
		} else {
			model.addAttribute("sort", sort.toString());
		}

		return "product/category";
	}

	/**
	 * 사용자 - 베스트를 눌러서 해당하는 도서 리스트 조회 (페이징)
	 * 정렬 불가
	 */
	@GetMapping("/best")
	public String getBestProducts(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponseProductReadDTO> response = productSearchService.getBestProducts(pageable);
		Page<ResponseProductReadDTO> products = PageResponseConverter.toPage(response);

		model.addAttribute("products", products);

		return "product/best-product";
	}

	/**
	 * 사용자 - 신상도서를 눌러서 해당하는 도서 리스트 조회 (페이징)
	 * 정렬 불가
	 */
	@GetMapping("/newest")
	public String getNewestProducts(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponseProductReadDTO> response = productSearchService.getNewestProducts(pageable);
		Page<ResponseProductReadDTO> products = PageResponseConverter.toPage(response);

		model.addAttribute("products", products);
		model.addAttribute("todayDate", LocalDate.now());

		return "product/newest-product";
	}
}
