package com.nhnacademy.front.product.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.product.category.model.RequestCategoryList;
import com.nhnacademy.front.product.category.model.dto.request.RequestCategoryDTO;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "카테고리", description = "관리자 카테고리 관련 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/categories")
public class AdminCategoryController {

	private final AdminCategoryService adminCategoryService;

	/**
	 * 관리자 페이지 -> 카테고리 뷰
	 * 등록 되어 있는 카테고리 리스트가 폴더 형식으로 보여짐
	 */
	@Operation(summary = "모든 카테고리 리스트 조회",
		description = "관리자 페이지에서 모든 카테고리 리스트를 폴더 구조로 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@GetMapping
	public String getCategories(Model model) {
		List<ResponseCategoryDTO> categories = adminCategoryService.getCategories();
		model.addAttribute("categories", categories);
		return "admin/product/categories";
	}

	/**
	 * 카테고리 생성
	 * 최상위 카테고리 + 하위 카테고리
	 */
	@Operation(summary = "최상위, 하위 카테고리 등록",
		description = "관리자 페이지에서 최상위 카테고리와 그 하위 카테고리를 등록합니다.",
		responses = {
			@ApiResponse(responseCode = "302", description = "카테고리 등록 후 카테고리 리스트 조회 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PostMapping
	public String createCategoryTree(@Parameter(description = "카테고리 리스트 생성 모델", required = true, schema = @Schema(implementation = RequestCategoryList.class))
		@Validated @ModelAttribute RequestCategoryList requests, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		adminCategoryService.createCategoryTree(requests.getCategories());
		return "redirect:/admin/settings/categories";
	}

	/**
	 * 카테고리 생성
	 * 존재하는 카테고리의 하위 카테고리
	 */
	@Operation(summary = "하위 카테고리 등록",
		description = "관리자 페이지에서 이미 존재하는 카테고리의 하위 카테고리를 등록합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "카테고리 등록 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PostMapping("/{category-id}")
	public ResponseEntity<Void> createChildCategory(@Parameter(description = "카테고리 등록 및 수정 모델", required = true, schema = @Schema(implementation = RequestCategoryDTO.class))
		@Validated @RequestBody RequestCategoryDTO request, BindingResult bindingResult,
		@Parameter(description = "등록할 카테고리의 상위 카테고리 ID", example = "1", required = true) @PathVariable("category-id") Long categoryId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		adminCategoryService.createChildCategory(categoryId, request);
		return ResponseEntity.ok().build();
	}

	/**
	 * 카테고리 수정 (카테고리명)
	 */
	@Operation(summary = "카테고리 수정",
		description = "관리자 페이지에서 카테고리의 이름을 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "카테고리명 수정 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PutMapping("/{category-id}")
	public ResponseEntity<Void> updateCategory(@Parameter(description = "카테고리 등록 및 수정 모델", required = true, schema = @Schema(implementation = RequestCategoryDTO.class))
		@Validated @RequestBody RequestCategoryDTO request, BindingResult bindingResult,
		@Parameter(description = "수정할 카테고리 ID", example = "1", required = true) @PathVariable("category-id") Long categoryId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		adminCategoryService.updateCategory(categoryId, request);
		return ResponseEntity.ok().build();
	}

	/**
	 * 카테고리 삭제 (자식 카테고리가 없는 경우 가능)
	 */
	@Operation(summary = "카테고리 삭제",
		description = "관리자 페이지에서 최하위 카테고리를 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@DeleteMapping("/{category-id}")
	public ResponseEntity<Void> deleteCategory(@Parameter(description = "삭제할 카테고리 ID", example = "5", required = true) @PathVariable("category-id") Long categoryId) {
		adminCategoryService.deleteCategory(categoryId);
		return ResponseEntity.ok().build();
	}

}
