package com.nhnacademy.front.coupon.coupon.controller;

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

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.coupon.coupon.model.dto.request.RequestCouponDTO;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;
import com.nhnacademy.front.coupon.coupon.service.CouponService;
import com.nhnacademy.front.coupon.couponpolicy.model.dto.ResponseCouponPolicyDTO;
import com.nhnacademy.front.coupon.couponpolicy.service.CouponPolicyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/mypage/coupons")
public class CouponController {

	private final CouponService couponServiceImpl;
	private final CouponPolicyService couponPolicyServiceImpl;

	/**
	 * 관리자 쿠폰 생성 뷰
	 */
	@GetMapping("/register")
	public String createCouponForm(Model model) {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
		PageResponse<ResponseCouponPolicyDTO> couponPolicyDTO = couponPolicyServiceImpl.getCouponPolicies(pageable);
		Page<ResponseCouponPolicyDTO> couponPolicies = PageResponseConverter.toPage(couponPolicyDTO);

		// 상준; 카테고리, 상품 정보도 받아오기 + coupon-register.html 수정

		model.addAttribute("couponPolicies", couponPolicies);
		return "admin/coupon/coupon-register";
	}

	/**
	 * 관리자 쿠폰 생성 post
	 */
	@PostMapping("/register")
	public String createCoupon(@Validated @ModelAttribute RequestCouponDTO request, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		couponServiceImpl.createCoupon(request);
		return "redirect:/admin/mypage/coupons";
	}

	/**
	 * 관리자 쿠폰 전체 조회
	 */
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
	@GetMapping("/{couponId}")
	public String getCoupon(@PathVariable Long couponId, Model model) {
		ResponseCouponDTO response = couponServiceImpl.getCoupon(couponId);

		model.addAttribute("coupon", response);
		return "admin/coupon/coupon-detail";
	}

	/**
	 * 쿠폰 활성 여부 변경
	 * 활성 <-> 비활성 상태 변경
	 */
	@PutMapping("/{couponId}")
	public ResponseEntity<Void> updateCoupon(@PathVariable Long couponId) {
		couponServiceImpl.updateCoupon(couponId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
