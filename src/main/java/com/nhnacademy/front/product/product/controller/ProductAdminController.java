package com.nhnacademy.front.product.product.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "도서(관리자)", description = "관리자 도서 관련 API")
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
	@Operation(summary = "모든 도서 리스트 조회",
		description = "관리자 페이지에서 모든 도서 리스트를 조회합니다. (검색 가능)",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@GetMapping
	public String getProducts(@Parameter(description = "페이징 정보") @PageableDefault(page = 0, size = 10) Pageable pageable, Model model,
		@Parameter(description = "검색 키워드", in = ParameterIn.QUERY) @RequestParam(required = false) String keyword) {
		PageResponse<ResponseProductReadDTO> response;
		if(Objects.isNull(keyword)) {
			response = productAdminService.getProducts(pageable);
		} else {
			response = productAdminService.getProductsBySearch(pageable, keyword);
		}
		Page<ResponseProductReadDTO> products = PageResponseConverter.toPage(response);

		model.addAttribute("products", products);
		return "admin/product/books/view";
	}

	/**
	 * 관리자 도서 정보 단일 조회
	 */
	@Operation(summary = "도서 단일 조회",
		description = "관리자 페이지에서 도서 상세페이지를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@GetMapping("/register/{book-id}")
	public String getProductsById(@Parameter(description = "조회할 도서 ID", example = "1", required = true) @PathVariable("book-id") Long bookId,
		Model model, @Parameter(description = "페이징 정보") Pageable pageable) {
		model.addAttribute("bookId", bookId);

		ResponseProductReadDTO response = productService.getProduct(bookId, "");
		model.addAttribute("product", response);

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

		model.addAttribute("product", response);
		return "admin/product/books/register";
	}

	/**
	 * 관리자 도서 등록 뷰로 이동
	 */
	@Operation(summary = "도서 등록 페이지 이동",
		description = "관리자 페이지에서 도서 등록 페이지로 이동합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@GetMapping("/register")
	public String getRegisterView(Model model, @Parameter(description = "페이징 정보") Pageable pageable) {
		ResponseProductReadDTO response = new ResponseProductReadDTO();
		model.addAttribute("product", response);

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
	@Operation(summary = "도서 등록",
		description = "관리자 페이지에서 도서를 등록합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PostMapping("/register")
	public String createProduct(@Parameter(description = "도서 등록 및 수정 DTO", required = true, schema = @Schema(implementation = RequestProductDTO.class))
		@Validated @ModelAttribute RequestProductDTO request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		productAdminService.createProduct(request);
		return "redirect:/admin/settings/books";
	}

	/**
	 * 관리자가 도서를 수정
	 */
	@Operation(summary = "도서 수정",
		description = "관리자 페이지에서 도서를 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PutMapping("/register/{book-id}")
	public String updateProduct(@Parameter(description = "도서 등록 및 수정 DTO", required = true, schema = @Schema(implementation = RequestProductDTO.class))
		@Validated @ModelAttribute RequestProductDTO request, BindingResult bindingResult,
		@Parameter(description = "수정할 도서 ID", example = "1", required = true) @PathVariable("book-id") Long bookId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		productAdminService.updateProduct(bookId, request);
		return "redirect:/admin/settings/books";
	}

	/**
	 * 관리자가 도서 판매가를 수정
	 */
	@Operation(summary = "도서 판매가 수정",
		description = "관리자 페이지에서 도서의 판매가를 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PutMapping("/{book-id}/salePrice")
	public String updateProduct(@Parameter(description = "도서 판매가 수정 DTO", required = true, schema = @Schema(implementation = RequestProductSalePriceUpdateDTO.class))
		@Validated @RequestBody RequestProductSalePriceUpdateDTO request, BindingResult bindingResult,
		@Parameter(description = "수정할 도서 ID", example = "1", required = true) @PathVariable("book-id") Long bookId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		productAdminService.updateProductSalePrice(bookId, request);
		return "redirect:/admin/settings/books";
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