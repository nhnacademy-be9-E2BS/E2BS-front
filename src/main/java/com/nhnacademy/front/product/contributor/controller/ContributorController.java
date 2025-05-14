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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.product.contributor.dto.request.RequestContributorDTO;
import com.nhnacademy.front.product.contributor.dto.response.ResponseContributorDTO;
import com.nhnacademy.front.product.contributor.position.dto.response.ResponsePositionDTO;
import com.nhnacademy.front.product.contributor.position.service.PositionService;
import com.nhnacademy.front.product.contributor.service.ContributorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/mypage/contributors")
public class ContributorController {
	private final ContributorService contributorService;
	private final PositionService positionService;

	@PostMapping
	public String createContributor(@Validated @ModelAttribute RequestContributorDTO requestContributorDTO,
		BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		List<ResponsePositionDTO> positions = positionService.getPositionList();
		ResponseContributorDTO result = contributorService.createContributor(requestContributorDTO);

		redirectAttributes.addFlashAttribute("positions", positions);
		return "redirect:/admin/mypage/contributors";
	}


	@PutMapping("/{contributorId}")
	public String updateContributor(@Validated @ModelAttribute RequestContributorDTO requestContributorDTO, @PathVariable Long contributorId, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		contributorService.updateContributor(contributorId, requestContributorDTO);
		return "redirect:/admin/mypage/contributors";
	}

	@GetMapping("/{contributorId}")
	public String getContributor(@PathVariable Long contributorId, Model model) {
		ResponseContributorDTO responseContributorDTO = contributorService.getContributor(contributorId);
		return "/admin/product/contributors";
	}

	@GetMapping()
	public String getAllContributors(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		PageResponse<ResponseContributorDTO> response = contributorService.getContributors(pageable);
		Page<ResponseContributorDTO> contributors = PageResponseConverter.toPage(response);

		if (!model.containsAttribute("positions")) {
			List<ResponsePositionDTO> positions = positionService.getPositionList();
			model.addAttribute("positions", positions);
		}

		model.addAttribute("contributors", contributors);
		return "/admin/product/contributors";
	}


}

