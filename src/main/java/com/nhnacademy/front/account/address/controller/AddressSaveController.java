package com.nhnacademy.front.account.address.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.address.exception.SaveAddressFailedException;
import com.nhnacademy.front.account.address.model.dto.request.RequestMemberAddressSaveDTO;
import com.nhnacademy.front.account.address.service.AddressService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원 배송지 정보 입력", description = "회원 배송지 정보 입력 및 저장 기능 제공")
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/addresses/save")
public class AddressSaveController {

	private final AddressService addressService;

	@Operation(summary = "회원 배송지 정보 입력 폼 페이지", description = "회원 배송지 정보 입력 화면 제공")
	@JwtTokenCheck
	@GetMapping
	public String getSaveMemberAddress(HttpServletRequest request,
		Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		model.addAttribute("memberId", memberId);

		return "member/mypage/mypage-addresses-save";
	}

	@Operation(summary = "회원 배송지 저장 요청 처리", description = "입력받은 배송지 정보를 바탕으로 배송지 저장",
		responses = {
			@ApiResponse(responseCode = "302", description = "배송지 저장 성공 후 배송지 관리 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "500", description = "배송지 저장 요청에 따른 응답 실패", content = @Content(schema = @Schema(implementation = SaveAddressFailedException.class)))
		})
	@JwtTokenCheck
	@PostMapping
	public String saveMemberAddress(@Validated
		@Parameter(description = "배송지 저장 요청 DTO", required = true, schema = @Schema(implementation = RequestMemberAddressSaveDTO.class))
		@ModelAttribute RequestMemberAddressSaveDTO requestMemberAddressSaveDTO,
		BindingResult bindingResult,
		HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		addressService.saveMemberAddress(memberId, requestMemberAddressSaveDTO);

		return "redirect:/mypage/addresses";
	}

}
