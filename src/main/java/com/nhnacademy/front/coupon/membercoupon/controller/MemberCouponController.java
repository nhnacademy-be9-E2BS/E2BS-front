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
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.coupon.coupon.model.dto.response.ResponseCouponDTO;
import com.nhnacademy.front.coupon.coupon.service.CouponService;
import com.nhnacademy.front.coupon.membercoupon.model.dto.request.RequestAllMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseMemberCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.service.MemberCouponService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberCouponController {

	private final CouponService couponService;
	private final MemberCouponService memberCouponService;

	/**
	 * 관리자 페이지 : 쿠폰 발급 뷰
	 * 쿠폰 리스트를 보고 쿠폰을 선택하여 쿠폰만료일 지정
	 */
	@JwtTokenCheck
	@GetMapping("/admin/settings/memberCoupons/issue")
	public String getMemberCouponsForm(@PageableDefault(size = 5) Pageable pageable, Model model) {
		PageResponse<ResponseCouponDTO> response = couponService.getCouponsIsActive(pageable);
		Page<ResponseCouponDTO> couponsIsActive = PageResponseConverter.toPage(response);

		model.addAttribute("couponsIsActive", couponsIsActive);
		return "admin/coupon/coupon-issue";
	}

	/**
	 * 관리자 페이지 : 활성 상태인 회원들에게 쿠폰 발급
	 */
	@JwtTokenCheck
	@PostMapping("/admin/settings/memberCoupons/issue")
	public String postMemberCoupons(@Validated RequestAllMemberCouponDTO request, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		memberCouponService.issueCouponToAllMember(request);
		return "redirect:/admin/settings/memberCoupons/issue";
	}

	/**
	 * 쿠폰함 : 사용자 전체 쿠폰 조회 뷰
	 */
	@JwtTokenCheck
	@GetMapping("/mypage/couponBox")
	public String getMemberCouponBox(HttpServletRequest request, @PageableDefault(size = 8) Pageable pageable, Model model, @RequestParam(required = false) Long status) {
		// JWT 토큰에서 Long ld 추출
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		PageResponse<ResponseMemberCouponDTO> response = memberCouponService.getUsableMemberCouponsByMemberId(memberId, pageable);
		Long usableCouponCount = response.getTotalElements();

		if (status == null || status == 1) {
			response = memberCouponService.getMemberCouponsByMemberId(memberId, pageable);
		} else if(status == 2) {
			response = memberCouponService.getUsableMemberCouponsByMemberId(memberId, pageable);
		} else if(status == 3) {
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
