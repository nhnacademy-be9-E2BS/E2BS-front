package com.nhnacademy.front.coupon.couponpolicy.controller;

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

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.RequestCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("admin/couponPolicies")
@RequiredArgsConstructor
public class CouponPolicyController {

	private final CouponPolicyService couponPolicyServiceImpl;

	/**
	 * 관리자 쿠폰 정책 등록
	 */
	@PostMapping
	public String createCouponPolicy(@Validated @ModelAttribute RequestCouponPolicyDTO requestCouponPolicyDTO,
	BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		couponPolicyServiceImpl.createCouponPolicy(requestCouponPolicyDTO);
		return "redirect:/admin/couponPolicies";
	}

	/**
	 * 관리자 정책 전체 조회
	 */
	@GetMapping
	public String getCouponPolicies(@PageableDefault(size = 5) Pageable pageable, Model model) {
		PageResponse<ResponseCouponPolicyDTO> pageResponse =
			couponPolicyServiceImpl.getCouponPolicies(pageable.getPageNumber(), pageable.getPageSize());

		model.addAttribute("couponPolicies", pageResponse.getContent());
		model.addAttribute("currentPage", pageResponse.getNumber());
		model.addAttribute("totalPages", pageResponse.getTotalPages());
		return "admin/coupon/coupon-policy";
	}

	/**
	 * 관리자 정책 단건 조회
	 */
	@GetMapping("/{couponPolicyId}")
	public String getCouponPolicyById(@PathVariable Long couponPolicyId, Model model) {
		ResponseCouponPolicyDTO responseDTO = couponPolicyServiceImpl.getCouponPolicyById(couponPolicyId);
		model.addAttribute("couponPolicies", responseDTO);
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalPages", 1);
		return "admin/coupon/coupon-policy";
	}


}
