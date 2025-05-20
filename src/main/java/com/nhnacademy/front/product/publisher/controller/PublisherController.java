package com.nhnacademy.front.product.publisher.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.product.publisher.model.dto.request.RequestPublisherDTO;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.publisher.service.PublisherService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/publishers")
public class PublisherController {

	private final PublisherService publisherService;

	/**
	 * 관리자 페이지 -> 출판사 뷰
	 * 등록 되어 있는 출판사 리스트가 보여짐
	 */
	@JwtTokenCheck
	@GetMapping
	public String getPublishers(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponsePublisherDTO> response = publisherService.getPublishers(pageable);
		Page<ResponsePublisherDTO> publishers = PageResponseConverter.toPage(response);

		model.addAttribute("publishers", publishers);
		return "admin/product/publishers";
	}

	/**
	 * 출판사 생성
	 */
	@JwtTokenCheck
	@PostMapping
	public String createPublisher(@Validated @ModelAttribute RequestPublisherDTO requestPublisherDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		publisherService.createPublisher(requestPublisherDTO);

		return "redirect:/admin/settings/publishers";
	}

	/**
	 * 출판사 수정
	 */
	@JwtTokenCheck
	@PutMapping("/{publisherId}")
	public ResponseEntity<Void> updatePublisher(@Validated @RequestBody RequestPublisherDTO requestPublisherDTO,
		BindingResult bindingResult, @PathVariable Long publisherId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		publisherService.updatePublisher(publisherId, requestPublisherDTO);

		return ResponseEntity.ok().build();
	}
}
