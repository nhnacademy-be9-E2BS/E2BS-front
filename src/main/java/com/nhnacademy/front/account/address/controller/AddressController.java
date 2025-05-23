package com.nhnacademy.front.account.address.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.address.model.dto.request.RequestMemberAddressSaveDTO;
import com.nhnacademy.front.account.address.model.dto.response.ResponseMemberAddressDTO;
import com.nhnacademy.front.account.address.service.AddressService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/addresses")
public class AddressController {

	private final AddressService addressService;

	@JwtTokenCheck
	@GetMapping
	public String getMemberAddresses(HttpServletRequest request, Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		List<ResponseMemberAddressDTO> addresses = addressService.getMemberAddresses(memberId);

		model.addAttribute("memberId", memberId);
		model.addAttribute("addresses", addresses);

		return "member/mypage/mypage-addresses";
	}

	@JwtTokenCheck
	@GetMapping("/{addressId}")
	public String getUpdateMemberAddress(@PathVariable("addressId") long addressId,
		HttpServletRequest request, Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		ResponseMemberAddressDTO address = addressService.getAddressByAddressId(memberId, addressId);

		model.addAttribute("memberId", memberId);
		model.addAttribute("address", address);

		return "member/mypage/mypage-addresses-update";
	}

	@JwtTokenCheck
	@PutMapping("/{addressId}")
	public String updateMemberAddress(
		@Validated @ModelAttribute RequestMemberAddressSaveDTO requestMemberAddressSaveDTO,
		@PathVariable("addressId") long addressId,
		HttpServletRequest request) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		addressService.updateAddress(requestMemberAddressSaveDTO, memberId, addressId);

		return "redirect:/mypage/addresses";
	}

	@JwtTokenCheck
	@DeleteMapping("/{addressId}")
	public String deleteAddress(@PathVariable("addressId") long addressId, HttpServletRequest request) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		addressService.deleteAddress(memberId, addressId);

		return "redirect:/mypage/addresses";
	}

}
