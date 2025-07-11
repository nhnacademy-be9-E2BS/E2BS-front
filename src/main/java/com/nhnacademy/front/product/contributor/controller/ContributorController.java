package com.nhnacademy.front.product.contributor.controller;

import java.util.List;

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

import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.product.contributor.dto.request.RequestContributorDTO;
import com.nhnacademy.front.product.contributor.dto.response.ResponseContributorDTO;
import com.nhnacademy.front.product.contributor.position.dto.response.ResponsePositionDTO;
import com.nhnacademy.front.product.contributor.position.service.PositionService;
import com.nhnacademy.front.product.contributor.service.ContributorService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "기여자(관리자)", description = "관리자 기여자 관련 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/contributors")
public class ContributorController {
	private final ContributorService contributorService;
	private final PositionService positionService;

	/**
	 * 기여자 생성
	 */
	@PostMapping
	public String createContributor(@Validated @ModelAttribute RequestContributorDTO requestContributorDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		contributorService.createContributor(requestContributorDTO);

		return "redirect:/admin/settings/contributors";
	}

	/**
	 * 기여자 이름 or 역할 수정
	 */
	@PutMapping("/{contributorId}")
	public String updateContributor(@Validated @ModelAttribute RequestContributorDTO request,
		@PathVariable Long contributorId, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		contributorService.updateContributor(contributorId, request);
		return "redirect:/admin/settings/contributors";
	}

	/**
	 * 기여자 단건 조회
	 */
	@GetMapping("/{contributorId}")
	public String getContributor(@PathVariable Long contributorId, Model model) {
		contributorService.getContributor(contributorId);
		return "admin/product/contributors";
	}

	/**기여자 전체 조회
	 */
	@GetMapping
	public String getAllContributors(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		List<ResponsePositionDTO> positions = positionService.getPositionList();
		model.addAttribute("positions", positions);

		PageResponse<ResponseContributorDTO> response = contributorService.getContributors(pageable);
		Page<ResponseContributorDTO> contributors = PageResponseConverter.toPage(response);

		model.addAttribute("contributors", contributors);
		return "admin/product/contributors";
	}

}

