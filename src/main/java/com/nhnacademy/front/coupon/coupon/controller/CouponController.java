package com.nhnacademy.front.coupon.coupon.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.coupon.coupon.model.dto.request.RequestCouponDTO;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;
import com.nhnacademy.front.coupon.coupon.service.CouponService;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.response.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;
import com.nhnacademy.front.elasticsearch.service.ProductSearchService;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductAdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "쿠폰", description = "쿠폰 관리 페이지 및 기능 제공")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/coupons")
public class CouponController {

	private final CouponService couponServiceImpl;
	private final CouponPolicyService couponPolicyServiceImpl;
	private final AdminCategoryService adminCategoryService;
	private final ProductAdminService productAdminService;
	private final ProductSearchService productSearchService;

	/**
	 * 관리자 쿠폰 생성 뷰
	 */
	@Operation(summary = "쿠폰 생성 폼 페이지", description = "관리자가 쿠폰 타입에 따라 쿠폰 생성 기능 제공")
	@JwtTokenCheck
	@GetMapping("/register")
	public String createCouponForm(
		@PageableDefault Pageable pageable,
		@RequestParam(value = "couponType", required = false) String couponType,
		@RequestParam(value = "keyword", required = false) String keyword, Model model) {
		log.info("keyword : {}", keyword);
		Pageable pageable_fix = PageRequest.of(0, Integer.MAX_VALUE);
		PageResponse<ResponseCouponPolicyDTO> couponPolicyDTO = couponPolicyServiceImpl.getCouponPolicies(pageable_fix);
		Page<ResponseCouponPolicyDTO> couponPolicies = PageResponseConverter.toPage(couponPolicyDTO);

		List<ResponseCategoryDTO> categories = adminCategoryService.getCategories();

		PageResponse<ResponseProductReadDTO> productDTO = productAdminService.getProducts(pageable);
		Page<ResponseProductReadDTO> products = PageResponseConverter.toPage(productDTO);
		if(keyword != null) {
			PageResponse<ResponseProductReadDTO> response = productSearchService.getProductsBySearch(pageable, keyword, null, "");
			products = PageResponseConverter.toPage(response);
			log.info(String.valueOf(products.getTotalElements()));
		}

		if (couponType == null) {
			couponType = "total";
		}
		model.addAttribute("couponType", couponType);
		model.addAttribute("categories", categories);
		model.addAttribute("products", products);
		model.addAttribute("couponPolicies", couponPolicies);
		return "admin/coupon/coupon-register";
	}

	/**
	 * 관리자 쿠폰 생성 post
	 */
	@Operation(summary = "쿠폰 생성 요청 처리", description = "입력받은 쿠폰 정보를 바탕으로 쿠폰 생성",
	responses = {
		@ApiResponse(responseCode = "302", description = "쿠폰 생성 성공 후 쿠폰 조회 페이지로 리다이렉션"),
		@ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
	})
	@JwtTokenCheck
	@PostMapping("/register")
	public String createCoupon(
		@Parameter(description = "쿠폰 등록 요청 DTO", required = true, schema = @Schema(implementation = RequestCouponDTO.class))
		@Validated @ModelAttribute RequestCouponDTO request,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		couponServiceImpl.createCoupon(request);
		return "redirect:/admin/settings/coupons";
	}

	/**
	 * 관리자 쿠폰 전체 조회
	 */
	@Operation(summary = "관리자 쿠폰 전체 조회", description = "생성한 전체 쿠폰 조회")
	@JwtTokenCheck
	@GetMapping
	public String getCoupons(@PageableDefault() Pageable pageable, Model model) {
		PageResponse<ResponseCouponDTO> response = couponServiceImpl.getCoupons(pageable);
		Page<ResponseCouponDTO> coupons = PageResponseConverter.toPage(response);

		model.addAttribute("coupons", coupons);
		return "admin/coupon/coupon";
	}

	/**
	 * 관리자 쿠폰 ID로 단건 조회
	 */
	@Operation(summary = "쿠폰 단건 조회", description = "연결된 쿠폰 정보를 ID로 조회")
	@JwtTokenCheck
	@GetMapping("/{coupon-id}")
	public String getCoupon(
		@Parameter(description = "쿠폰 ID", example = "1")
		@PathVariable("coupon-id") Long couponId, Model model) {
		ResponseCouponDTO response = couponServiceImpl.getCoupon(couponId);

		model.addAttribute("coupon", response);
		return "admin/coupon/coupon-detail";
	}

	/**
	 * 쿠폰 활성 여부 변경
	 * 활성 <-> 비활성 상태 변경
	 */
	@Operation(summary = "쿠폰 활성/비활성 여부 변경", description = "쿠폰의 활성여부를 변경",
	responses = {
		@ApiResponse(responseCode = "200", description = "쿠폰 활성 여부 변경 성공"),
		@ApiResponse(responseCode = "400", description = "쿠폰 활성 상태 변경 실패")
	})
	@JwtTokenCheck
	@PutMapping("/{coupon-id}")
	public ResponseEntity<Void> updateCoupon(
		@Parameter(description = "쿠폰 ID", example = "1")
		@PathVariable("coupon-id") Long couponId) {
		couponServiceImpl.updateCoupon(couponId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
