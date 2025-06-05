package com.nhnacademy.front.coupon.couponpolicy.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.request.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.response.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("admin/settings/couponPolicies")
@RequiredArgsConstructor
public class CouponPolicyController {

	private final CouponPolicyService couponPolicyServiceImpl;

	/**
	 * 관리자 쿠폰 정책 등록
	 */
	@JwtTokenCheck
	@PostMapping
	public String createCouponPolicy(@Validated @ModelAttribute RequestCouponPolicyDTO requestCouponPolicyDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		couponPolicyServiceImpl.createCouponPolicy(requestCouponPolicyDTO);
		return "redirect:/admin/settings/couponPolicies";
	}

	/**
	 * 관리자 정책 전체 조회
	 */
	@JwtTokenCheck
	@GetMapping
	public String getCouponPolicies(@PageableDefault(size = 5) Pageable pageable, Model model) {
		PageResponse<ResponseCouponPolicyDTO> pageResponse = couponPolicyServiceImpl.getCouponPolicies(pageable);
		Page<ResponseCouponPolicyDTO> couponPolicies = PageResponseConverter.toPage(pageResponse);

		model.addAttribute("couponPolicies", couponPolicies);

		return "admin/coupon/coupon-policy";
	}

	/**
	 * 관리자 정책 단건 조회
	 */
	@JwtTokenCheck
	@GetMapping("/{couponPolicyId}")
	public String getCouponPolicyById(@PathVariable Long couponPolicyId, Model model) {
		ResponseCouponPolicyDTO responseDTO = couponPolicyServiceImpl.getCouponPolicyById(couponPolicyId);

		model.addAttribute("couponPolicy", responseDTO);

		return "admin/coupon/coupon-policy-detail";
	}

}
