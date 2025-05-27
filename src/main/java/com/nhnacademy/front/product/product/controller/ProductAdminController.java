package com.nhnacademy.front.product.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateByQueryDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductSalePriceUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchByQueryTypeDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchDTO;
import com.nhnacademy.front.product.product.service.ProductAdminService;
import com.nhnacademy.front.product.product.service.ProductService;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;
import com.nhnacademy.front.product.tag.service.TagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/books")
@Slf4j
public class ProductAdminController {

	private final ProductAdminService productAdminService;
	private final ProductService productService;
	private final AdminCategoryService adminCategoryService;
	private final TagService tagService;

	/**
	 * 관리자 페이지 -> 전체 도서 리스트 조회
	 */
	@JwtTokenCheck
	@GetMapping
	public String getProducts(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponseProductReadDTO> response = productAdminService.getProducts(pageable);
		Page<ResponseProductReadDTO> products = PageResponseConverter.toPage(response);

		model.addAttribute("products", products);
		return "admin/product/books/view";
	}

	/**
	 * 관리자 도서 단일 조회
	 */
	@JwtTokenCheck
	@GetMapping("/register/{bookId}")
	public String getProductById(@PathVariable Long bookId, Model model) {
		ResponseProductReadDTO response = productService.getProduct(bookId);

		model.addAttribute("product", response);
		return "admin/product/books/register";
	}

	/**
	 * 관리자 도서 등록 뷰로 이동
	 */
	@JwtTokenCheck
	@GetMapping("/register")
	public String getRegisterView() {

		return "admin/product/books/register";
	}

	/**
	 * 관리자가 admin settings 페이지에서 도서 등록
	 * 관리자 -> 도서 등록
	 */
	@JwtTokenCheck
	@PostMapping("/register")
	public String createProduct(@Validated @ModelAttribute RequestProductDTO request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		productAdminService.createProduct(request);
		return "admin/product/books/register";
	}

	/**
	 * 관리자가 도서를 수정
	 */
	@JwtTokenCheck
	@PutMapping("/register/{bookId}")
	public ResponseEntity<Void> updateProduct(@Validated @RequestBody RequestProductDTO request,
		BindingResult bindingResult, @PathVariable Long bookId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		productAdminService.updateProduct(bookId, request);
		return ResponseEntity.ok().build();
	}

	/**
	 * 관리자가 도서 판매가를 수정
	 */
	@JwtTokenCheck
	@PutMapping("/{bookId}/salePrice")
	public ResponseEntity<Void> updateProduct(@Validated @RequestBody RequestProductSalePriceUpdateDTO request,
		BindingResult bindingResult, @PathVariable Long bookId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		return ResponseEntity.ok().build();
	}


	/**
	 * 관리자가 알라딘 api로 도서 조회
	 */

	@GetMapping("/aladdin/search")
	public String searchProducts(@ModelAttribute RequestProductApiSearchDTO request,@PageableDefault(page = 0, size = 10)Pageable pageable, Model model) {
		PageResponse<ResponseProductsApiSearchDTO> response = productService.getProductsApi(request, pageable);
		Page<ResponseProductsApiSearchDTO> products = PageResponseConverter.toPage(response);
		model.addAttribute("products", products);
		return "admin/product/books/books-api-search";
	}

	@GetMapping("/aladdin/list")
	public String searchProductsByQuery(@ModelAttribute RequestProductApiSearchByQueryTypeDTO request,@PageableDefault(page = 0, size = 10)Pageable pageable, Model model) {

		PageResponse<ResponseProductsApiSearchByQueryTypeDTO> response = productService.getProductsApi(request, pageable);
		Page<ResponseProductsApiSearchByQueryTypeDTO> products = PageResponseConverter.toPage(response);
		model.addAttribute("products", products);
		model.addAttribute("queryType", request.getQueryType());
		return "admin/product/books/books-api-search-query";
	}


	@GetMapping("/search")
	public String showSearchForm() {
		return "admin/product/search";
	}

	@PostMapping("/aladdin/register")
	public String showRegisterForm(@ModelAttribute RequestProductApiCreateDTO dto, Model model) {

		model.addAttribute("book", dto);

		List<ResponseCategoryDTO> categories = adminCategoryService.getCategories();
		model.addAttribute("categories", categories);

		List<ResponseTagDTO> tags = tagService.getTags(Pageable.unpaged()).getContent();
		model.addAttribute("tags", tags);
		return "/admin/product/books/books-api-register";
	}

	@PostMapping("/aladdin/register/list")
	public String showRegisterForm(@ModelAttribute RequestProductApiCreateByQueryDTO dto, Model model) {

		model.addAttribute("book", dto);
		List<ResponseCategoryDTO> categories = adminCategoryService.getCategories();
		model.addAttribute("categories", categories);

		List<ResponseTagDTO> tags = tagService.getTags(Pageable.unpaged()).getContent();
		model.addAttribute("tags", tags);
		return "/admin/product/books/books-api-register-query";
	}

	@PostMapping("/aladdin/register/submit/list")
	public String submitBook(@ModelAttribute RequestProductApiCreateByQueryDTO dto) {
		log.info("🔍 queryType from form: {}", dto.getQueryType()); // ← 여기

		productService.createProductQueryApi(dto);
		return "redirect:/admin/settings/books/search";
	}

	@PostMapping("/aladdin/register/submit")
	public String submitBook(@ModelAttribute RequestProductApiCreateDTO dto) {
		productService.createProductApi(dto);
		return "redirect:/admin/settings/books/search";
	}

}
