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
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.product.publisher.model.dto.request.RequestPublisherDTO;
import com.nhnacademy.front.product.publisher.model.dto.response.ResponsePublisherDTO;
import com.nhnacademy.front.product.publisher.service.PublisherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "출판사", description = "출판사 관련 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/companies")
public class PublisherController {

	private final PublisherService publisherService;

	/**
	 * 관리자 페이지 -> 출판사 뷰
	 * 등록 되어 있는 출판사 리스트가 보여짐
	 */
	@Operation(summary = "모든 출판사 리스트 조회",
		description = "관리자 페이지에서 모든 출판사 리스트를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@GetMapping
	public String getPublishers(@Parameter(description = "페이징 정보") @PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponsePublisherDTO> response = publisherService.getPublishers(pageable);
		Page<ResponsePublisherDTO> publishers = PageResponseConverter.toPage(response);

		model.addAttribute("publishers", publishers);
		return "admin/product/publishers";
	}

	/**
	 * 출판사 생성
	 */
	@Operation(summary = "출판사 등록",
		description = "관리자 페이지에서 출판사를 등록합니다.",
		responses = {
			@ApiResponse(responseCode = "302", description = "출판사 등록 후 출판사 조회 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PostMapping
	public String createPublisher(@Parameter(description = "출판사 등록 및 수정 DTO", required = true, schema = @Schema(implementation = RequestPublisherDTO.class))
		@Validated @ModelAttribute RequestPublisherDTO requestPublisherDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		publisherService.createPublisher(requestPublisherDTO);

		return "redirect:/admin/settings/companies";
	}

	/**
	 * 출판사 수정
	 */
	@Operation(summary = "출판사 수정",
		description = "관리자 페이지에서 출판사명을 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "출판사명 수정 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PutMapping("/{publisherId}")
	public ResponseEntity<Void> updatePublisher(@Parameter(description = "출판사 등록 및 수정 DTO", required = true, schema = @Schema(implementation = RequestPublisherDTO.class))
		@Validated @RequestBody RequestPublisherDTO requestPublisherDTO, BindingResult bindingResult,
		@Parameter(description = "수정할 출판사 ID", example = "1", required = true) @PathVariable Long publisherId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		publisherService.updatePublisher(publisherId, requestPublisherDTO);

		return ResponseEntity.ok().build();
	}
}
