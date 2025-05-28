package com.nhnacademy.front.order.wrapper.controller;

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
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestModifyWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestRegisterWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;
import com.nhnacademy.front.order.wrapper.service.WrapperService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/papers")
@Slf4j
public class WrapperController {

	private final WrapperService wrapperService;

	/**
	 * 관리자 페이지 -> 포장지 뷰
	 * 등록 되어 있는 모든 포장지 리스트가 보여짐 (판매 여부 상관 없음)
	 */
	@JwtTokenCheck
	@GetMapping
	public String getWrappers(@PageableDefault(page = 0, size = 5) Pageable pageable, Model model) {
		log.info("포장지 조회 요청 controller start");
		PageResponse<ResponseWrapperDTO> response = wrapperService.getWrappers(pageable);
		log.info("service 응답");
		log.info(response.toString());
		Page<ResponseWrapperDTO> wrappers = PageResponseConverter.toPage(response);

		model.addAttribute("wrappers", wrappers);
		return "admin/product/wrappers";
	}

	/**
	 * 포장지 생성
	 */
	@JwtTokenCheck
	@PostMapping
	public String createWrapper(@Validated @ModelAttribute RequestRegisterWrapperDTO requestRegisterWrapperDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		wrapperService.createWrapper(requestRegisterWrapperDTO);

		return "redirect:/admin/settings/papers";
	}

	/**
	 * 포장지 수정 (판매 여부만 수정 가능)
	 */
	@JwtTokenCheck
	@PutMapping("/{wrapperId}")
	public ResponseEntity<Void> updateWrapper(@Validated @RequestBody RequestModifyWrapperDTO requestModifyWrapperDTO,
		BindingResult bindingResult, @PathVariable Long wrapperId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		wrapperService.updateWrapper(wrapperId, requestModifyWrapperDTO);

		return ResponseEntity.ok().build();
	}
}
