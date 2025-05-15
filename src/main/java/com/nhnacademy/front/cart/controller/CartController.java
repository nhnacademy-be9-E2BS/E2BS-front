package com.nhnacademy.front.cart.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.order.RequestCartOrderDTO;
import com.nhnacademy.front.cart.service.CustomerCartService;
import com.nhnacademy.front.cart.service.GuestCartService;
import com.nhnacademy.front.common.exception.ValidationFailedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {

	private final MemberService memberService;
	private final CustomerCartService customerCartService;
	private final GuestCartService guestCartService;

	/**
	 * 장바구니 항목 추가
	 */
	@PostMapping("/carts/items")
	public ResponseEntity<?> addToCart(@Validated @RequestBody RequestAddCartItemsDTO requestDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		/// 고객인 경우
		// requestDto.setCustomerId(1L);
		// customerCartService.createCartItemForCustomer(requestDto);

		
		/// 게스트인 경우
		requestDto.setSessionId("guest-session-abc123");
		guestCartService.createCartItemForGuest(requestDto);
		
		return ResponseEntity.ok().body(Map.of("message", "success"));
	}

	@PostMapping("/orders/create")
	public ResponseEntity<?> order(@RequestBody RequestCartOrderDTO requestDto) {
		log.info("dto: {}", requestDto.toString());
		return ResponseEntity.ok().body(Map.of("message", "success"));
	}

}
