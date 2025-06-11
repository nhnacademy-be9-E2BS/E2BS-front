package com.nhnacademy.front.account.address.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.account.address.exception.DeleteAddressFailedException;
import com.nhnacademy.front.account.address.exception.UpdateAddressFailedException;
import com.nhnacademy.front.account.address.model.dto.request.RequestMemberAddressSaveDTO;
import com.nhnacademy.front.account.address.model.dto.response.ResponseMemberAddressDTO;
import com.nhnacademy.front.account.address.service.AddressService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.EmptyResponseException;
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

@Tag(name = "회원 배송지 관리", description = "회원 배송지 관리 페이지 및 기능 제공")
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/addresses")
public class AddressController {

	private final AddressService addressService;

	@Operation(summary = "회원 배송지 관리 페이지", description = "회원 배송지 관리 화면 제공")
	@JwtTokenCheck
	@GetMapping
	public String getMemberAddresses(HttpServletRequest request, Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		List<ResponseMemberAddressDTO> addresses = addressService.getMemberAddresses(memberId);

		model.addAttribute("memberId", memberId);
		model.addAttribute("addresses", addresses);

		return "member/mypage/mypage-addresses";
	}

	@Operation(summary = "회원 배송지 세부 폼 페이지", description = "회원 배송지 세부 정보 화면 제공")
	@JwtTokenCheck
	@GetMapping("/{address-id}")
	public String getUpdateMemberAddress(@PathVariable("address-id") long addressId,
		HttpServletRequest request, Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		ResponseMemberAddressDTO address = addressService.getAddressByAddressId(memberId, addressId);

		model.addAttribute("memberId", memberId);
		model.addAttribute("address", address);

		return "member/mypage/mypage-addresses-update";
	}

	@Operation(summary = "회원 배송지 수정 기능", description = "회원 배송지 정보 수정 기능 제공",
		responses = {
			@ApiResponse(responseCode = "302", description = "회원 배송지 정보 수정 후 배송지 관리 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "500", description = "요청에 따른 응답 실패", content = @Content(schema = @Schema(implementation = UpdateAddressFailedException.class)))
		})
	@JwtTokenCheck
	@PutMapping("/{address-id}")
	public String updateMemberAddress(@Validated
		@Parameter(description = "배송지 세부 정보 수정 요청 DTO", required = true, schema = @Schema(implementation = RequestMemberAddressSaveDTO.class))
		@ModelAttribute RequestMemberAddressSaveDTO requestMemberAddressSaveDTO,
		BindingResult bindingResult,
		@PathVariable("address-id") long addressId,
		HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		addressService.updateAddress(requestMemberAddressSaveDTO, memberId, addressId);

		return "redirect:/mypage/addresses";
	}

	@Operation(summary = "회원 배송지 정보 삭제 기능", description = "회원 배송지 정보 삭제 기능 제공",
		responses = {
			@ApiResponse(responseCode = "302", description = "회원 배송지 정보 삭제 후 배송지 관리 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "500", description = "요청에 따른 응답 실패", content = @Content(schema = @Schema(implementation = DeleteAddressFailedException.class)))
		})
	@JwtTokenCheck
	@DeleteMapping("/{address-id}")
	public String deleteAddress(@PathVariable("address-id") long addressId, HttpServletRequest request) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		addressService.deleteAddress(memberId, addressId);

		return "redirect:/mypage/addresses";
	}

	@Operation(summary = "회원 기본 배송지 설정 기능", description = "회원 기본 배송지 설정 기능 제공",
		responses = {
			@ApiResponse(responseCode = "302", description = "회원 기본 배송지 설정 후 배송지 관리 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "500", description = "요청에 따른 응답 실패", content = @Content(schema = @Schema(implementation = EmptyResponseException.class)))
		})
	@JwtTokenCheck
	@PostMapping("/default")
	public String setDefaultAddress(@RequestParam("addressId") Long addressId,
		HttpServletRequest request) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		addressService.setDefaultAddress(memberId, addressId);

		return "redirect:/mypage/addresses";
	}

}
