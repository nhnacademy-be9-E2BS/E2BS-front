package com.nhnacademy.front.coupon.couponpolicy.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.request.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.response.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "쿠폰 정책", description = "쿠폰 정책 등록, 조회, 단건 조회 기능 제공")
@Controller
@RequestMapping("admin/settings/couponPolicies")
@RequiredArgsConstructor
public class CouponPolicyController {

	private final CouponPolicyService couponPolicyServiceImpl;

	/**
	 * 쿠폰 정책 등록 처리
	 */
	@Operation(
		summary = "쿠폰 정책 등록",
		description = "입력받은 정보를 바탕으로 쿠폰 정책 생성",
		responses = {
			@ApiResponse(responseCode = "302", description = "쿠폰 정책 생성 성공 후 목록 페이지로 리다이렉트"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
		}
	)
	@JwtTokenCheck
	@PostMapping
	public String createCouponPolicy(
		@Parameter(description = "쿠폰 정책 등록 요청 DTO", required = true, schema = @Schema(implementation = RequestCouponPolicyDTO.class))
		@Validated @ModelAttribute RequestCouponPolicyDTO requestCouponPolicyDTO,
		BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		couponPolicyServiceImpl.createCouponPolicy(requestCouponPolicyDTO);
		return "redirect:/admin/settings/couponPolicies";
	}

	/**
	 * 전체 쿠폰 정책 조회
	 */
	@Operation(
		summary = "쿠폰 정책 목록 조회",
		description = "등록된 모든 쿠폰 정책을 페이징하여 조회",
		responses = {
			@ApiResponse(responseCode = "200", description = "쿠폰 정책 목록 페이지 반환")
		}
	)
	@JwtTokenCheck
	@GetMapping
	public String getCouponPolicies(
		@Parameter(description = "페이징 정보 (기본 size=5)")
		@PageableDefault(size = 5) Pageable pageable,
		Model model) {

		PageResponse<ResponseCouponPolicyDTO> pageResponse = couponPolicyServiceImpl.getCouponPolicies(pageable);
		Page<ResponseCouponPolicyDTO> couponPolicies = PageResponseConverter.toPage(pageResponse);
		model.addAttribute("couponPolicies", couponPolicies);
		return "admin/coupon/coupon-policy";
	}

	/**
	 * 단건 쿠폰 정책 상세 조회
	 */
	@Operation(
		summary = "쿠폰 정책 상세 조회",
		description = "쿠폰 정책 ID를 기반으로 단건 상세 정보 조회",
		responses = {
			@ApiResponse(responseCode = "200", description = "쿠폰 정책 상세 페이지 반환"),
			@ApiResponse(responseCode = "404", description = "해당 ID의 쿠폰 정책이 존재하지 않음")
		}
	)
	@JwtTokenCheck
	@GetMapping("/{coupon-policy-id}")
	public String getCouponPolicyById(
		@Parameter(description = "쿠폰 정책 ID", example = "1")
		@PathVariable("coupon-policy-id") Long couponPolicyId,
		Model model) {

		ResponseCouponPolicyDTO responseDTO = couponPolicyServiceImpl.getCouponPolicyById(couponPolicyId);
		model.addAttribute("couponPolicy", responseDTO);
		return "admin/coupon/coupon-policy-detail";
	}
}
