package com.nhnacademy.front.pointpolicy.controller;

import java.util.List;

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

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.pointpolicy.model.domain.PointPolicyType;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyRegisterDTO;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyUpdateDTO;
import com.nhnacademy.front.pointpolicy.model.dto.response.ResponsePointPolicyDTO;
import com.nhnacademy.front.pointpolicy.service.PointPolicyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PointPolicyController {

	private final PointPolicyService pointPolicyService;

	/**
	 * 포인트 정책 전체 리스트 조회
	 */
	@JwtTokenCheck
	@GetMapping("/admin/settings/pointPolicies")
	public String getPointPolicies(Model model) {
		List<ResponsePointPolicyDTO> responseRegister = pointPolicyService.getRegisterPointPolicy();
		List<ResponsePointPolicyDTO> responseReviewImg = pointPolicyService.getReviewImgPointPolicy();
		List<ResponsePointPolicyDTO> responseReview = pointPolicyService.getReviewPointPolicy();
		List<ResponsePointPolicyDTO> responseBook = pointPolicyService.getBookPointPolicy();

		model.addAttribute("pointPolicies_register", responseRegister);
		model.addAttribute("pointPolicies_review_img", responseReviewImg);
		model.addAttribute("pointPolicies_review", responseReview);
		model.addAttribute("pointPolicies_book", responseBook);
		return "admin/pointpolicy/point-policy";
	}

	/**
	 * 포인트 정책 생성 뷰
	 */
	@JwtTokenCheck
	@GetMapping("/admin/settings/pointPolicies/register")
	public String getPointPoliciesRegisterForm(Model model) {

		model.addAttribute("pointPolicyTypes", PointPolicyType.values());
		return "admin/pointpolicy/point-policy-register";
	}

	/**
	 *  포인트 정책 생성
	 */
	@JwtTokenCheck
	@PostMapping("/admin/settings/pointPolicies/register")
	public String createPointPolicies(@Validated @ModelAttribute RequestPointPolicyRegisterDTO request, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		pointPolicyService.createPointPolicy(request);
		return "redirect:/admin/settings/point-policies";
	}

	/**
	 * 포인트 정책 활성여부 수정
	 */
	@JwtTokenCheck
	@PutMapping("/admin/settings/pointPolicies/{pointPolicyId}/activate")
	public ResponseEntity<Void> activatePointPolicy(@PathVariable("pointPolicyId") Long pointPolicyId) {
		pointPolicyService.activatePointPolicy(pointPolicyId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/admin/settings/pointPolicies/{pointPolicyId}")
	public ResponseEntity<Void> updatePointPolicy(@PathVariable("pointPolicyId") Long pointPolicyId,
		@Validated @ModelAttribute RequestPointPolicyUpdateDTO request,
		BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		pointPolicyService.updatePointPolicy(pointPolicyId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
