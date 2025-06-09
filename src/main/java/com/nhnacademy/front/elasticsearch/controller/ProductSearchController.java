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
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.jwt.parser.JwtHasToken;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "도서 검색 및 정렬", description = "엘라스틱 서치 도서 관련 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/books/search")
public class ProductSearchController {

	private final ProductSearchService productSearchService;
	private final UserCategoryService userCategoryService;

	/**
	 * 사용자 - 검색창에 검색어를 입력하여 도서 리스트 조회 (페이징)
	 * 정렬 기준 선택 가능 (default = 정확도순)
	 */
	@Operation(summary = "검색어로 도서 검색 및 정렬",
		description = "검색어로 도서 리스트 조회 후 정렬 (정렬은 선택사항)",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터", content = @Content(schema = @Schema(implementation = ProductSortType.class))),
		})
	@GetMapping
	public String getProductsBySearch(@Parameter(description = "페이징 정보") @PageableDefault(page = 0, size = 10) Pageable pageable, Model model,
		@Parameter(description = "검색 키워드", required = true, in = ParameterIn.QUERY) @RequestParam String keyword,
		@Parameter(description = "정렬 기준", in = ParameterIn.QUERY) @RequestParam(required = false) ProductSortType sort,
		@Parameter(hidden = true) HttpServletRequest request) {
		String memberId = "";
		if (!JwtHasToken.hasToken(request)) {
			memberId = JwtGetMemberId.jwtGetMemberId(request);
		}

		PageResponse<ResponseProductReadDTO> response = productSearchService.getProductsBySearch(pageable, keyword, sort, memberId);
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
	 * 정렬 기준 선택 가능
	 */
	@Operation(summary = "카테고리 ID로 도서 검색 및 정렬",
		description = "카테고리 헤더를 누르면 해당 도서 리스트 조회 후 정렬 (정렬은 선택사항)",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터", content = @Content(schema = @Schema(implementation = ProductSortType.class))),
		})
	@GetMapping("/category/{category-id}")
	public String getProductByCategory(@Parameter(description = "페이징 정보") @PageableDefault(page = 0, size = 10) Pageable pageable, Model model,
		@Parameter(description = "조회할 카테고리 ID", required = true, in = ParameterIn.QUERY) @PathVariable("category-id") Long categoryId,
		@Parameter(description = "정렬 기준", in = ParameterIn.QUERY) @RequestParam(required = false) ProductSortType sort,
		@Parameter(hidden = true) HttpServletRequest request) {
		String memberId = "";
		if (!JwtHasToken.hasToken(request)) {
			memberId = JwtGetMemberId.jwtGetMemberId(request);
		}

		PageResponse<ResponseProductReadDTO> response = productSearchService.getProductsByCategory(pageable, categoryId, sort, memberId);
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
	@Operation(summary = "베스트 도서 조회",
		description = "헤더에서 '베스트'를 누르면 베스트 도서 리스트 조회",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공")
		})
	@GetMapping("/best")
	public String getBestProducts(@Parameter(description = "페이징 정보") @PageableDefault(page = 0, size = 10) Pageable pageable, Model model,
		@Parameter(hidden = true) HttpServletRequest request) {
		String memberId = "";
		if (!JwtHasToken.hasToken(request)) {
			memberId = JwtGetMemberId.jwtGetMemberId(request);
		}

		PageResponse<ResponseProductReadDTO> response = productSearchService.getBestProducts(pageable, memberId);
		Page<ResponseProductReadDTO> products = PageResponseConverter.toPage(response);

		model.addAttribute("products", products);

		return "product/best-product";
	}

	/**
	 * 사용자 - 신상도서를 눌러서 해당하는 도서 리스트 조회 (페이징)
	 * 정렬 불가
	 */
	@Operation(summary = "신상 도서 조회",
		description = "헤더에서 '신상'을 누르면 신상 도서 리스트 조회",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공")
		})
	@GetMapping("/newest")
	public String getNewestProducts(@Parameter(description = "페이징 정보") @PageableDefault(page = 0, size = 10) Pageable pageable, Model model,
		@Parameter(hidden = true) HttpServletRequest request) {
		String memberId = "";
		if (!JwtHasToken.hasToken(request)) {
			memberId = JwtGetMemberId.jwtGetMemberId(request);
		}

		PageResponse<ResponseProductReadDTO> response = productSearchService.getNewestProducts(pageable, memberId);
		Page<ResponseProductReadDTO> products = PageResponseConverter.toPage(response);

		model.addAttribute("products", products);
		model.addAttribute("todayDate", LocalDate.now());

		return "product/newest-product";
	}
}
