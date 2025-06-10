package com.nhnacademy.front.product.tag.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.product.tag.model.dto.request.RequestTagDTO;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;
import com.nhnacademy.front.product.tag.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "태그", description = "태그 관련 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/tags")
public class TagController {

	private final TagService tagService;

	/**
	 * 관리자가 현재 등록된 태그들을 조회
	 * 관리자 마이페이지 -> 태그
	 */
	@Operation(summary = "모든 태그 리스트 조회", description = "관리자 페이지에서 모든 태그 리스트를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@GetMapping
	public String getTags(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponseTagDTO> response = tagService.getTags(pageable);
		Page<ResponseTagDTO> tags = PageResponseConverter.toPage(response);

		model.addAttribute("tags", tags);
		return "admin/product/tags";
	}

	/**
	 * 태그 생성
	 */
	@Operation(summary = "태그 등록",
		description = "관리자 페이지에서 태그를 등록합니다.",
		responses = {
			@ApiResponse(responseCode = "302", description = "태그 등록 후 태그 조회 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PostMapping
	public String createTag(@Validated @ModelAttribute("tag") RequestTagDTO requestTagDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		tagService.createTag(requestTagDTO);

		return "redirect:/admin/settings/tags";
	}

	/**
	 * 태그 수정
	 */
	@Operation(summary = "태그 수정",
		description = "관리자 페이지에서 태그명을 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "태그명 수정 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PutMapping("/{tagId}")
	public ResponseEntity<Void> updateTag(@Validated @RequestBody RequestTagDTO requestTagDTO,
		BindingResult bindingResult, @PathVariable Long tagId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		tagService.updateTag(tagId, requestTagDTO);

		return ResponseEntity.ok().build();
	}

	/**
	 * 태그 삭제
	 */
	@Operation(summary = "태그 삭제",
		description = "관리자 페이지에서 태그를 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "태그 삭제 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@DeleteMapping("/{tagId}")
	public ResponseEntity<Void> deleteTag(@PathVariable Long tagId,
		@Validated @RequestBody RequestTagDTO requestTagDTO) {
		tagService.deleteTag(tagId, requestTagDTO);

		return ResponseEntity.ok().build();
	}

}

