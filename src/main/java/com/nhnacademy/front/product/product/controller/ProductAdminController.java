package com.nhnacademy.front.product.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.product.product.model.dto.request.RequestProductCreateDTO;
import com.nhnacademy.front.product.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/books")
public class ProductAdminController {

	private final ProductService productService;

	/**
	 * 관리자가 admin settings 페이지에서 도서 관리
	 * 관리자 -> books
	 */

	/**
	 * 관리자가 도서를 생성
	 */
	@PostMapping("/self")
	public String createProduct(@Validated @ModelAttribute("book")RequestProductCreateDTO request,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		productService.createProduct(request);

		return "redirect:/admin/settings/books/self";
	}

	/**
	 *
	 */
}
