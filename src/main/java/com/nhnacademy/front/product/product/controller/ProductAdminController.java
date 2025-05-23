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

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductApiSearchDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductCreateDTO;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductUpdateDTO;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductsApiSearchDTO;
import com.nhnacademy.front.product.product.service.ProductService;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;
import com.nhnacademy.front.product.tag.service.TagService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/books")
public class ProductAdminController {

	private final ProductService productService;
	private final AdminCategoryService adminCategoryService;
	private final TagService tagService;

	

	/**
	 * 관리자가 admin settings 페이지에서 도서 등록
	 * 관리자 -> 도서 등록
	 */
	@GetMapping("/register")
	public String getProducts(Model model) {
		List<ResponseCategoryDTO> categories = adminCategoryService.getCategories();
		model.addAttribute("categories", categories);

		return "admin/product/books/register";
	}


	/**
	 * 관리자가 도서를 생성
	 */
	@PostMapping("/register")
	public String createProduct(@Validated @ModelAttribute("book") RequestProductCreateDTO request,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		productService.createProduct(request);

		return "redirect:/admin/settings/books/register";
	}

	/**
	 * 관리자가 도서를 수정
	 */
	@PutMapping("/{bookId}")
	public ResponseEntity<Void> updateProduct(@Validated @RequestBody RequestProductUpdateDTO request,
		BindingResult bindingResult, @PathVariable Long productId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		productService.updateProduct(productId, request);

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

	@PostMapping("/aladdin/register/submit")
	public String submitBook(@ModelAttribute RequestProductApiCreateDTO dto) {
		productService.createProductApi(dto);
		return "redirect:/admin/settings/books/search";
	}
}
