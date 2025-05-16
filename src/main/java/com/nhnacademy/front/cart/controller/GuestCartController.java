package com.nhnacademy.front.cart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartItemsForGuestDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForGuestDTO;
import com.nhnacademy.front.cart.service.GuestCartService;
import com.nhnacademy.front.common.exception.ValidationFailedException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GuestCartController {

	private final GuestCartService guestCartService;


	/**
	 * 게스트 장바구니 항목 추가
	 */
	@PostMapping("/guests/carts/items")
	public ResponseEntity<Void> guestAddToCart(@Validated @RequestBody RequestAddCartItemsDTO requestDto, BindingResult bindingResult,
		HttpSession httpSession) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		requestDto.setSessionId(httpSession.getId());
		guestCartService.createCartItemForGuest(requestDto);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 게스트 장바구니 목록 조회
	 */
	@GetMapping("/guests/carts")
	public String getCartsByGuest(Model model, HttpSession httpSession) {
		String sessionId = httpSession.getId();
		List<ResponseCartItemsForGuestDTO> cartItemsByGuest = guestCartService.getCartItemsByGuest(sessionId);

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
	public ResponseEntity<Void> updateCartItemForGuest(@Validated @RequestBody RequestUpdateCartItemsDTO requestDto, BindingResult bindingResult,
													HttpSession httpSession) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		requestDto.setSessionId(httpSession.getId());
		guestCartService.updateCartItemForGuest(requestDto);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 게스트 장바구니 항목 삭제
	 */
	@DeleteMapping("/guests/carts/items")
	public ResponseEntity<Void> deleteCartItemForGuest(@Validated @RequestBody RequestDeleteCartItemsForGuestDTO requestDto, BindingResult bindingResult,
													HttpSession httpSession) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		requestDto.setSessionId(httpSession.getId());
		guestCartService.deleteCartItemForGuest(requestDto);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 게스트 장바구니 전체 삭제
	 */
	@DeleteMapping("/guests/carts")
	public ResponseEntity<Void> deleteCartForCustomer(HttpSession httpSession) {
		String sessionId = httpSession.getId();
		guestCartService.deleteCartForGuest(sessionId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
