package com.nhnacademy.front.coupon.membercoupon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;
import com.nhnacademy.front.coupon.coupon.service.CouponService;
import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.service.MemberCouponService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원 쿠폰", description = "회원 쿠폰 발급 및 조회 기능 제공")
@Controller
@RequiredArgsConstructor
public class MemberCouponController {

	private final CouponService couponService;
	private final MemberCouponService memberCouponService;

	@Operation(
		summary = "회원 쿠폰 발급 폼 페이지",
		description = "활성 쿠폰 목록을 조회하여 쿠폰 발급 폼을 제공",
		responses = {
			@ApiResponse(responseCode = "200", description = "쿠폰 발급 폼 페이지 반환")
		}
	)
	@JwtTokenCheck
	@GetMapping("/admin/settings/memberCoupons/issue")
	public String getMemberCouponsForm(@PageableDefault(size = 5) Pageable pageable, Model model) {
		PageResponse<ResponseCouponDTO> response = couponService.getCouponsIsActive(pageable);
		Page<ResponseCouponDTO> couponsIsActive = PageResponseConverter.toPage(response);

		model.addAttribute("couponsIsActive", couponsIsActive);
		return "admin/coupon/coupon-issue";
	}

	@Operation(
		summary = "회원 전체 쿠폰 발급",
		description = "모든 활성 회원에게 쿠폰을 발급",
		responses = {
			@ApiResponse(responseCode = "302", description = "발급 성공 시 발급 폼 페이지로 리다이렉트"),
			@ApiResponse(responseCode = "400", description = "요청 값 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
		}
	)
	@JwtTokenCheck
	@PostMapping("/admin/settings/memberCoupons/issue")
	public String postMemberCoupons(
		@Parameter(description = "전체 회원 쿠폰 발급 요청 DTO", required = true, schema = @Schema(implementation = RequestAllMemberCouponDTO.class))
		@Validated RequestAllMemberCouponDTO request,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		memberCouponService.issueCouponToAllMember(request);
		return "redirect:/admin/settings/memberCoupons/issue";
	}

	@Operation(
		summary = "회원 쿠폰함 조회",
		description = "로그인한 사용자의 쿠폰함 내 쿠폰 리스트 조회 (전체/사용가능/사용불가)",
		responses = {
			@ApiResponse(responseCode = "200", description = "쿠폰함 조회 성공")
		}
	)
	@JwtTokenCheck
	@GetMapping("/mypage/couponBox")
	public String getMemberCouponBox(
		HttpServletRequest request,
		@PageableDefault(size = 8) Pageable pageable,
		Model model,
		@Parameter(description = "쿠폰 상태 필터 (1:전체, 2:사용가능, 3:사용불가)", example = "1")
		@RequestParam(required = false) Long status) {

		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		PageResponse<ResponseMemberCouponDTO> response = memberCouponService.getUsableMemberCouponsByMemberId(memberId, pageable);
		Long usableCouponCount = response.getTotalElements();

		if (status == null || status == 1) {
			response = memberCouponService.getMemberCouponsByMemberId(memberId, pageable);
		} else if (status == 2) {
			response = memberCouponService.getUsableMemberCouponsByMemberId(memberId, pageable);
		} else if (status == 3) {
			response = memberCouponService.getUnusableMemberCouponsByMemberId(memberId, pageable);
		} else {
			response = memberCouponService.getMemberCouponsByMemberId(memberId, pageable);
		}

		Page<ResponseMemberCouponDTO> memberCoupons = PageResponseConverter.toPage(response);

		model.addAttribute("memberCoupons", memberCoupons);
		model.addAttribute("usableCouponCount", usableCouponCount);
		model.addAttribute("status", status);
		return "member/mypage/coupon-box";
	}
}
