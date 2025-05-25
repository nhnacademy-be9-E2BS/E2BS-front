package com.nhnacademy.front.order.deliveryfee.controller;

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
import org.springframework.web.bind.annotation.PostMapping;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.order.deliveryfee.model.dto.request.RequestDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DeliveryFeeController {

	private final DeliveryFeeSevice deliveryFeeSevice;

	/**
	 * 관리자의 배송비 정책 조회 페이지
	 */
	@JwtTokenCheck
	@GetMapping("/admin/settings/deliveryFee")
	public String getDeliveryFees(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {
		ResponseEntity<PageResponse<ResponseDeliveryFeeDTO>> response = deliveryFeeSevice.getDeliveryFees(pageable);
		PageResponse<ResponseDeliveryFeeDTO> pageResponse = response.getBody();
		Page<ResponseDeliveryFeeDTO> deliveryFees = PageResponseConverter.toPage(pageResponse);
		model.addAttribute("deliveryFees", deliveryFees);
		return "admin/order/deliveryFeeManagement";
	}

	/**
	 * 관리자의 배송비 정책 추가 요청 처리
	 */
	@JwtTokenCheck
	@PostMapping("/admin/settings/deliveryFee")
	public String createDeliveryFees(@Validated @ModelAttribute RequestDeliveryFeeDTO deliveryFeeDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		deliveryFeeSevice.CreateDeliveryFee(deliveryFeeDTO);
		return "redirect:/admin/settings/deliveryFee";
	}

}
