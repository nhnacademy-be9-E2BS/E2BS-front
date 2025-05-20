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

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.product.tag.model.dto.request.RequestTagDTO;
import com.nhnacademy.front.product.tag.model.dto.response.ResponseTagDTO;
import com.nhnacademy.front.product.tag.service.TagService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/mypage/tags")
public class TagController {

	private final TagService tagService;

	/**
	 * 관리자가 현재 등록된 태그들을 조회
	 * 관리자 마이페이지 -> 태그
	 */
	@GetMapping
	public String getTags(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponseTagDTO> response = tagService.getTags(pageable);
		Page<ResponseTagDTO> tags = PageResponseConverter.toPage(response);

		model.addAttribute("tags", tags);
		return "admin/tags";
	}

	/**
	 * 태그 생성
	 */
	@PostMapping
	public String createTag(@Validated @ModelAttribute("tag") RequestTagDTO requestTagDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		tagService.createTag(requestTagDTO);

		return "redirect:/admin/mypage/tags";
	}

	/**
	 * 태그 수정
	 */
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
	@DeleteMapping("/{tagId}")
	public ResponseEntity<Void> deleteTag(@PathVariable Long tagId, @Validated @RequestBody RequestTagDTO requestTagDTO) {
		tagService.deleteTag(tagId, requestTagDTO);

		return ResponseEntity.ok().build();
	}

}

