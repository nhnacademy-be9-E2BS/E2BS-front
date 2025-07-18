package com.nhnacademy.front.product.contributor.position.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.product.contributor.position.dto.request.RequestPositionDTO;
import com.nhnacademy.front.product.contributor.position.dto.response.ResponsePositionDTO;
import com.nhnacademy.front.product.contributor.position.service.PositionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "역할(관리자)", description = "관리자 기여자의 역할 관련 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/positions")
public class PositionController {
	private final PositionService positionService;

	/**
	 * position 등록
	 */
	@JwtTokenCheck
	@PostMapping
	public String createPosition(@Validated @ModelAttribute RequestPositionDTO requestPositionDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		positionService.createPosition(requestPositionDTO);
		return "redirect:/admin/settings/positions";
	}

	/**
	 * position 수정
	 */
	@JwtTokenCheck
	@PutMapping("/{positionId}")
	public String updatePosition(@Validated @ModelAttribute RequestPositionDTO requestPositionDTO,
		@PathVariable long positionId, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		positionService.updatePosition(positionId, requestPositionDTO);
		return "redirect:/admin/settings/positions";
	}

	/**
	 * position 단건 조회
	 */
	@JwtTokenCheck
	@GetMapping("/{positionId}")
	public String getPosition(@PathVariable Long positionId, Model model) {
		ResponsePositionDTO responsePositionDTO = positionService.getPositionById(positionId);

		model.addAttribute("position", responsePositionDTO);
		model.addAttribute("positions", Page.empty());
		return "admin/product/positions";
	}

	/**
	 * position 리스트 조회 - 페이징
	 */
	@JwtTokenCheck
	@GetMapping()
	public String getPositions(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponsePositionDTO> response = positionService.getPositions(pageable);
		Page<ResponsePositionDTO> positions = PageResponseConverter.toPage(response);

		model.addAttribute("positions", positions);
		return "admin/product/positions";
	}

}
