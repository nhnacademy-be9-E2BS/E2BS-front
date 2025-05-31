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
import com.nhnacademy.front.product.contributor.dto.response.ResponseContributorDTO;
import com.nhnacademy.front.product.contributor.service.ContributorService;
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
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.publisher.service.PublisherService;
import com.nhnacademy.front.product.state.model.dto.response.ResponseProductStateDTO;
import com.nhnacademy.front.product.state.model.dto.service.ProductStateService;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;
import com.nhnacademy.front.product.tag.service.TagService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/books")
public class ProductAdminController {

	private final ProductAdminService productAdminService;
	private final ProductService productService;
	private final AdminCategoryService adminCategoryService;
	private final PublisherService publisherService;
	private final TagService tagService;
	private final ContributorService contributorService;
	private final ProductStateService productStateService;

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
	 * 관리자 도서 정보 단일 조회
	 */
	@JwtTokenCheck
	@GetMapping("/register/{bookId}")
	public String getProductsById(@PathVariable Long bookId, Model model) {
		ResponseProductReadDTO response = productService.getProduct(bookId);

		model.addAttribute("product", response);
		return "admin/product/books/register";
	}

	/**
	 * 관리자 도서 등록 뷰로 이동
	 */
	@JwtTokenCheck
	@GetMapping("/register")
	public String getRegisterView(Model model, Pageable pageable) {
		RequestProductApiCreateDTO dto = new RequestProductApiCreateDTO();
		model.addAttribute("book", dto);

		PageResponse<ResponseContributorDTO> contributors = contributorService.getContributors(pageable);
		model.addAttribute("contributors", contributors.getContent());

		PageResponse<ResponsePublisherDTO> publishers = publisherService.getPublishers(pageable);
		model.addAttribute("publishers", publishers.getContent());

		List<ResponseCategoryDTO> categories = adminCategoryService.getCategories();
		model.addAttribute("categories", categories);

		List<ResponseTagDTO> tags = tagService.getTags(Pageable.unpaged()).getContent();
		model.addAttribute("tags", tags);

		List<ResponseProductStateDTO> states = productStateService.getProductStates();
		model.addAttribute("states", states);
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
		return "redirect:/admin/settings/books";
	}

	/**
	 * 관리자가 도서를 수정
	 */
	@JwtTokenCheck
	@PutMapping("/register/{bookId}")
	public ResponseEntity<Void> updateProduct(@Validated @ModelAttribute RequestProductDTO request,
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
		productAdminService.updateProductSalePrice(bookId, request);
		return ResponseEntity.ok().build();
	}

	/**
	 * 관리자가 알라딘 api로 도서 조회
	 */
	@JwtTokenCheck
	@GetMapping("/aladdin/search")
	public String searchProducts(@ModelAttribute RequestProductApiSearchDTO request,
		@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponseProductsApiSearchDTO> response = productAdminService.getProductsApi(request, pageable);
		Page<ResponseProductsApiSearchDTO> products = PageResponseConverter.toPage(response);
		model.addAttribute("products", products);
		return "admin/product/books/books-api-search";
	}

	@JwtTokenCheck
	@GetMapping("/aladdin/list")
	public String searchProductsByQuery(@ModelAttribute RequestProductApiSearchByQueryTypeDTO request,
		@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {

		PageResponse<ResponseProductsApiSearchByQueryTypeDTO> response = productAdminService.getProductsApi(request,
			pageable);
		Page<ResponseProductsApiSearchByQueryTypeDTO> products = PageResponseConverter.toPage(response);
		model.addAttribute("products", products);
		model.addAttribute("queryType", request.getQueryType());
		return "admin/product/books/books-api-search-query";
	}

	@JwtTokenCheck
	@GetMapping("/search")
	public String showSearchForm() {
		return "admin/product/search";
	}

	@JwtTokenCheck
	@PostMapping("/aladdin/register")
	public String showRegisterForm(@ModelAttribute RequestProductApiCreateDTO dto, Model model) {

		model.addAttribute("book", dto);

		List<ResponseCategoryDTO> categories = adminCategoryService.getCategories();
		model.addAttribute("categories", categories);

		List<ResponseTagDTO> tags = tagService.getTags(Pageable.unpaged()).getContent();
		model.addAttribute("tags", tags);
		return "admin/product/books/books-api-register";
	}

	@JwtTokenCheck
	@PostMapping("/aladdin/register/list")
	public String showRegisterForm(@ModelAttribute RequestProductApiCreateByQueryDTO dto, Model model) {

		model.addAttribute("book", dto);
		List<ResponseCategoryDTO> categories = adminCategoryService.getCategories();
		model.addAttribute("categories", categories);

		List<ResponseTagDTO> tags = tagService.getTags(Pageable.unpaged()).getContent();
		model.addAttribute("tags", tags);
		return "admin/product/books/books-api-register-query";
	}

	@JwtTokenCheck
	@PostMapping("/aladdin/register/submit/list")
	public String submitBook(@ModelAttribute RequestProductApiCreateByQueryDTO dto) {

		productAdminService.createProductQueryApi(dto);
		return "redirect:/admin/settings/books/search";
	}

	@JwtTokenCheck
	@PostMapping("/aladdin/register/submit")
	public String submitBook(@ModelAttribute RequestProductApiCreateDTO dto) {
		productAdminService.createProductApi(dto);
		return "redirect:/admin/settings/books/search";
	}

}