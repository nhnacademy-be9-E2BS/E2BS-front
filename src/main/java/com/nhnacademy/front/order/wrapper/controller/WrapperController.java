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
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestModifyWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.request.RequestRegisterWrapperDTO;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;
import com.nhnacademy.front.order.wrapper.service.WrapperService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "포장지", description = "포장지 관련 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/settings/papers")
public class WrapperController {

	private final WrapperService wrapperService;

	/**
	 * 관리자 페이지 -> 포장지 뷰
	 * 등록 되어 있는 모든 포장지 리스트가 보여짐 (판매 여부 상관 없음)
	 */
	@Operation(summary = "모든 포장지 리스트 조회",
		description = "관리자 페이지에서 모든 포장지 리스트를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@GetMapping
	public String getWrappers(@Parameter(description = "페이징 정보") @PageableDefault(page = 0, size = 5) Pageable pageable, Model model) {
		PageResponse<ResponseWrapperDTO> response = wrapperService.getWrappers(pageable);
		Page<ResponseWrapperDTO> wrappers = PageResponseConverter.toPage(response);

		model.addAttribute("wrappers", wrappers);
		return "admin/product/wrappers";
	}

	/**
	 * 포장지 생성
	 */
	@Operation(summary = "포장지 등록",
		description = "관리자 페이지에서 포장지를 등록합니다.",
		responses = {
			@ApiResponse(responseCode = "302", description = "포장지 등록 후 포장지 조회 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PostMapping
	public String createWrapper(@Parameter(description = "포장지 등록 DTO", required = true, schema = @Schema(implementation = RequestRegisterWrapperDTO.class))
		@Validated @ModelAttribute RequestRegisterWrapperDTO requestRegisterWrapperDTO,
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
	@Operation(summary = "포장지 수정",
		description = "관리자 페이지에서 포장지의 판매 여부를 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "포장지 수정 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@JwtTokenCheck
	@PutMapping("/{wrapper-id}")
	public ResponseEntity<Void> updateWrapper(@Parameter(description = "포장지 수정 DTO", required = true, schema = @Schema(implementation = RequestModifyWrapperDTO.class))
		@Validated @RequestBody RequestModifyWrapperDTO requestModifyWrapperDTO, BindingResult bindingResult,
		@Parameter(description = "수정할 포장지 ID", example = "1", required = true) @PathVariable("wrapper-id") Long wrapperId) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		wrapperService.updateWrapper(wrapperId, requestModifyWrapperDTO);

		return ResponseEntity.ok().build();
	}
}
