package com.nhnacademy.front.product.category.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.product.category.model.dto.request.RequestCategoryDTO;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryDTO;
import com.nhnacademy.front.product.category.service.AdminCategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/categories")
public class AdminCategoryController {

	private final AdminCategoryService adminCategoryService;

	@GetMapping
	public String getCategories(Model model) {
		List<ResponseCategoryDTO> categories = adminCategoryService.getCategories();
		model.addAttribute("categories", categories);
		return "admin/categories";
	}

	@PostMapping
	public String createCategoryTree(@Validated @ModelAttribute List<RequestCategoryDTO> requests,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		adminCategoryService.createCategoryTree(requests);
		return "redirect:/admin/settings/categories";
	}

	@PostMapping("/{categoryId}")
	public String createChildCategory(@Validated @ModelAttribute RequestCategoryDTO request,
		BindingResult bindingResult, @PathVariable Long categoryId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		adminCategoryService.createChildCategory(categoryId, request);
		return "redirect:/admin/settings/categories";
	}

	@PutMapping("/{categoryId}")
	public String updateCategory(@Validated @ModelAttribute RequestCategoryDTO request,
		BindingResult bindingResult, @PathVariable Long categoryId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		adminCategoryService.updateCategory(categoryId, request);
		return "redirect:/admin/settings/categories";
	}

	@DeleteMapping("/{categoryId}")
	public String deleteCategory(@PathVariable Long categoryId) {
		adminCategoryService.deleteCategory(categoryId);
		return "redirect:/admin/settings/categories";
	}
}
