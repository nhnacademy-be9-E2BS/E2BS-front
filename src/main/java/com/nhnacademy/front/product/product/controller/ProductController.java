package com.nhnacademy.front.product.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class ProductController {
	@GetMapping
	public String getBookDetails() {
		return "product/product-detail";
	}
}
