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
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.product.category.model.RequestCategoryList;
import com.nhnacademy.front.product.category.model.dto.request.RequestCategoryDTO;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/categories")
public class AdminCategoryController {

	private final AdminCategoryService adminCategoryService;

	/**
	 * 관리자 페이지 -> 카테고리 뷰
	 * 등록 되어 있는 카테고리 리스트가 폴더 형식으로 보여짐
	 */
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
	@JwtTokenCheck
	@PostMapping
	public String createCategoryTree(@Validated @ModelAttribute RequestCategoryList requests,
		BindingResult bindingResult) {
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
	@JwtTokenCheck
	@PostMapping("/{categoryId}")
	public ResponseEntity<Void> createChildCategory(@Validated @RequestBody RequestCategoryDTO request,
		BindingResult bindingResult, @PathVariable Long categoryId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		adminCategoryService.createChildCategory(categoryId, request);
		return ResponseEntity.ok().build();
	}

	/**
	 * 카테고리 수정 (카테고리명)
	 */
	@JwtTokenCheck
	@PutMapping("/{categoryId}")
	public ResponseEntity<Void> updateCategory(@Validated @RequestBody RequestCategoryDTO request,
		BindingResult bindingResult, @PathVariable Long categoryId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		adminCategoryService.updateCategory(categoryId, request);
		return ResponseEntity.ok().build();
	}

	/**
	 * 카테고리 삭제 (자식 카테고리가 없는 경우 가능)
	 */
	@JwtTokenCheck
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
		adminCategoryService.deleteCategory(categoryId);
		return ResponseEntity.ok().build();
	}

}
