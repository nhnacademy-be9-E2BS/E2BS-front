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
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;
import com.nhnacademy.front.coupon.coupon.service.CouponService;
import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.service.MemberCouponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/member-coupons")
public class MemberCouponController {

	private final CouponService couponService;
	private final MemberCouponService memberCouponService;

	/**
	 * 관리자 페이지 : 쿠폰 발급 뷰
	 * 쿠폰 리스트를 보고 쿠폰을 선택하여 쿠폰만료일 지정
	 */
	@GetMapping("/issue")
	public String getMemberCouponsForm(@PageableDefault(size = 5) Pageable pageable, Model model) {
		PageResponse<ResponseCouponDTO> response = couponService.getCouponsIsActive(pageable);
		Page<ResponseCouponDTO> couponsIsActive = PageResponseConverter.toPage(response);

		model.addAttribute("couponsIsActive", couponsIsActive);
		return "admin/coupon/coupon-issue";
	}

	/**
	 * 관리자 페이지 : 활성 상태인 회원들에게 쿠폰 발급
	 */
	@PostMapping("/issue")
	public String postMemberCoupons(@Validated RequestAllMemberCouponDTO request, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		memberCouponService.issueCouponToAllMember(request);
		return "redirect:/admin/settings/member-coupons/issue";
	}
}
