package com.nhnacademy.front.cart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartItemsForGuestDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForGuestDTO;
import com.nhnacademy.front.cart.service.GuestCartService;
import com.nhnacademy.front.common.exception.ValidationFailedException;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GuestCartController {

	private final MemberService memberService;
	private final GuestCartService guestCartService;

	/**
	 * 게스트 장바구니 목록 조회
	 */
	@GetMapping("/guests/carts")
	public String guestCarts(Model model) {
		/// sessionId를 받아야함
		List<ResponseCartItemsForGuestDTO> cartItemsByGuest = guestCartService.getCartItemsByGuest("guest-session-abc123");

		long totalPaymentAmount = 0;
		for (ResponseCartItemsForGuestDTO cartItem : cartItemsByGuest) {
			totalPaymentAmount += cartItem.getProductTotalPrice();
		}

		model.addAttribute("cartItemsByGuest", cartItemsByGuest);
		model.addAttribute("totalPaymentAmount", totalPaymentAmount);

		return "cart/guest-cart";
	}

	/**
	 * 게스트 장바구니 항목 수량 변경
	 */
	@PutMapping("/guests/carts/items")
	public ResponseEntity<?> updateCartItemForGuest(@Validated @RequestBody RequestUpdateCartItemsDTO requestDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		/// sessionId를 가져와야함
		requestDto.setSessionId("guest-session-abc123");
		guestCartService.updateCartItemForGuest(requestDto);

		return ResponseEntity.ok().body(Map.of("message", "success"));
	}

	/**
	 * 게스트 장바구니 항목 삭제
	 */
	@DeleteMapping("/guests/carts/items")
	public ResponseEntity<?> deleteCartItemForGuest(@Validated @RequestBody RequestDeleteCartItemsForGuestDTO requestDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		/// sessionId를 가져와야함
		requestDto.setSessionId("guest-session-abc123");
		guestCartService.deleteCartItemForGuest(requestDto);

		return ResponseEntity.ok().body(Map.of("message", "success"));
	}

	/**
	 * 게스트 장바구니 전체 삭제
	 */
	@DeleteMapping("/guests/carts")
	public ResponseEntity<?> deleteCartForCustomer() {
		/// sessionId를 가져와야함
		String sessionId = "guest-session-abc123";
		guestCartService.deleteCartForGuest(sessionId);

		return ResponseEntity.ok().body(Map.of("message", "success"));
	}

}
