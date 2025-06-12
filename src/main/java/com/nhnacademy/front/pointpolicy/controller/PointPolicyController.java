package com.nhnacademy.front.pointpolicy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.pointpolicy.model.domain.PointPolicyType;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyRegisterDTO;
import com.nhnacademy.front.pointpolicy.model.dto.request.RequestPointPolicyUpdateDTO;
import com.nhnacademy.front.pointpolicy.model.dto.response.ResponsePointPolicyDTO;
import com.nhnacademy.front.pointpolicy.service.PointPolicyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "포인트 정책", description = "포인트 정책 조회, 등록, 수정 기능 제공")
@Controller
@RequiredArgsConstructor
public class PointPolicyController {

	private final PointPolicyService pointPolicyService;

	@Operation(
		summary = "포인트정책 전체 조회",
		description = "회원가입, 리뷰, 이미지 리뷰, 기본 적립률 정책을 그룹별로 조회",
		responses = {
			@ApiResponse(responseCode = "200", description = "정책 목록 뷰 반환")
		}
	)
	@JwtTokenCheck
	@GetMapping("/admin/settings/pointPolicies")
	public String getPointPolicies(Model model) {
		final String TITLE = "title";
		final String POLICIES = "policies";

		List<ResponsePointPolicyDTO> responseRegister = pointPolicyService.getRegisterPointPolicy();
		List<ResponsePointPolicyDTO> responseReviewImg = pointPolicyService.getReviewImgPointPolicy();
		List<ResponsePointPolicyDTO> responseReview = pointPolicyService.getReviewPointPolicy();
		List<ResponsePointPolicyDTO> responseBook = pointPolicyService.getBookPointPolicy();

		List<Map<String, Object>> sections = new ArrayList<>();
		sections.add(Map.of(TITLE, "회원가입 정책", POLICIES, responseRegister));
		sections.add(Map.of(TITLE, "이미지 리뷰 정책", POLICIES, responseReviewImg));
		sections.add(Map.of(TITLE, "일반 리뷰 정책", POLICIES, responseReview));
		sections.add(Map.of(TITLE, "기본 적립률(%) 정책", POLICIES, responseBook));

		model.addAttribute("policySections", sections);
		return "admin/pointpolicy/point-policy";
	}

	@Operation(
		summary = "포인트 정책 등록 폼 페이지",
		description = "새로운 포인트 정책을 등록하기 위한 폼 페이지 반환"
	)
	@JwtTokenCheck
	@GetMapping("/admin/settings/pointPolicies/register")
	public String getPointPoliciesRegisterForm(Model model) {
		model.addAttribute("pointPolicyTypes", PointPolicyType.values());
		return "admin/pointpolicy/point-policy-register";
	}

	@Operation(
		summary = "포인트 정책 등록",
		description = "입력한 정책 데이터를 기반으로 포인트 정책 등록 처리",
		responses = {
			@ApiResponse(responseCode = "302", description = "정책 등록 후 목록으로 리다이렉트"),
			@ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
		}
	)
	@JwtTokenCheck
	@PostMapping("/admin/settings/pointPolicies/register")
	public String createPointPolicies(
		@Parameter(description = "포인트 정책 등록 요청 DTO", required = true, schema = @Schema(implementation = RequestPointPolicyRegisterDTO.class))
		@Validated @ModelAttribute RequestPointPolicyRegisterDTO request,
		BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		pointPolicyService.createPointPolicy(request);
		return "redirect:/admin/settings/pointPolicies";
	}

	@Operation(
		summary = "포인트정책 활성화/비활성화",
		description = "정책의 활성화 상태를 토글",
		responses = {
			@ApiResponse(responseCode = "200", description = "정책 활성화 상태 변경 성공")
		}
	)
	@JwtTokenCheck
	@PutMapping("/admin/settings/pointPolicies/{point-policy-id}/activate")
	public ResponseEntity<Void> activatePointPolicy(
		@Parameter(description = "포인트 정책 ID", example = "1")
		@PathVariable("point-policy-id") Long pointPolicyId) {
		pointPolicyService.activatePointPolicy(pointPolicyId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(
		summary = "포인트 정책 수치 수정",
		description = "정책 수치 수정",
		responses = {
			@ApiResponse(responseCode = "200", description = "정책 수정 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
		}
	)
	@PutMapping("/admin/settings/pointPolicies/{point-policy-id}")
	public ResponseEntity<Void> updatePointPolicy(
		@Parameter(description = "포인트 정책 ID", example = "1")
		@PathVariable("point-policy-id") Long pointPolicyId,
		@Parameter(description = "포인트 정책 수정 요청 DTO", required = true, schema = @Schema(implementation = RequestPointPolicyUpdateDTO.class))
		@Validated @ModelAttribute RequestPointPolicyUpdateDTO request,
		BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		pointPolicyService.updatePointPolicy(pointPolicyId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
