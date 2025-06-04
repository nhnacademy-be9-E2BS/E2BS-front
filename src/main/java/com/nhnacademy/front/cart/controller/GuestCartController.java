package com.nhnacademy.front.cart.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
import com.nhnacademy.front.common.util.GuestCookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GuestCartController {

	private final GuestCartService guestCartService;

	private static final String CART_ITEMS_COUNTS = "cartItemsCounts";


	/**
	 * 게스트 장바구니 항목 추가
	 */
	@PostMapping("/guests/carts/items")
	public ResponseEntity<Integer> guestAddToCart(@Validated @RequestBody RequestAddCartItemsDTO requestDto, BindingResult bindingResult,
		                                          HttpServletRequest request, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		String guestKey = GuestCookieUtil.getGuestKey(request);
		if (Objects.isNull(guestKey)) {
			guestKey = UUID.randomUUID().toString();
			GuestCookieUtil.setGuestCookie(response, guestKey);
		}

		requestDto.setSessionId(guestKey);

		int cartItemsCounts = guestCartService.createCartItemForGuest(requestDto);
		request.getSession().setAttribute(CART_ITEMS_COUNTS, cartItemsCounts);

		return ResponseEntity.status(HttpStatus.CREATED).body(cartItemsCounts);
	}

	/**
	 * 게스트 장바구니 목록 조회
	 */
	@GetMapping("/guests/carts")
	public String getCartsByGuest(Model model, HttpServletRequest request, HttpServletResponse response) {
		String guestKey = GuestCookieUtil.getGuestKey(request);
		if (Objects.isNull(guestKey)) {
			guestKey = UUID.randomUUID().toString();
			GuestCookieUtil.setGuestCookie(response, guestKey);
		}

		List<ResponseCartItemsForGuestDTO> cartItemsByGuest = guestCartService.getCartItemsByGuest(guestKey);

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
	public ResponseEntity<Integer> updateCartItemForGuest(@Validated @RequestBody RequestUpdateCartItemsDTO requestDto, BindingResult bindingResult,
		                                               HttpServletRequest request, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		String guestKey = GuestCookieUtil.getGuestKey(request);
		if (Objects.isNull(guestKey)) {
			guestKey = UUID.randomUUID().toString();
			GuestCookieUtil.setGuestCookie(response, guestKey);
		}

		requestDto.setSessionId(guestKey);

		int cartItemsCounts = guestCartService.updateCartItemForGuest(requestDto);
		request.getSession().setAttribute(CART_ITEMS_COUNTS, cartItemsCounts);

		return ResponseEntity.ok(cartItemsCounts);
	}

	/**
	 * 게스트 장바구니 항목 삭제
	 */
	@DeleteMapping("/guests/carts/items")
	public ResponseEntity<Integer> deleteCartItemForGuest(@Validated @RequestBody RequestDeleteCartItemsForGuestDTO requestDto, BindingResult bindingResult,
		                                               HttpServletRequest request, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		String guestKey = GuestCookieUtil.getGuestKey(request);
		if (Objects.isNull(guestKey)) {
			guestKey = UUID.randomUUID().toString();
			GuestCookieUtil.setGuestCookie(response, guestKey);
		}

		requestDto.setSessionId(guestKey);
		guestCartService.deleteCartItemForGuest(requestDto);

		Integer cartItemsCounts = (Integer)request.getSession().getAttribute(CART_ITEMS_COUNTS);
		request.getSession().setAttribute(CART_ITEMS_COUNTS, cartItemsCounts-1);

		return ResponseEntity.ok(cartItemsCounts-1);
	}

	/**
	 * 게스트 장바구니 전체 삭제
	 */
	@DeleteMapping("/guests/carts")
	public ResponseEntity<Void> deleteCartForGuest(HttpServletRequest request, HttpServletResponse response) {
		String guestKey = GuestCookieUtil.getGuestKey(request);
		if (Objects.isNull(guestKey)) {
			guestKey = UUID.randomUUID().toString();
			GuestCookieUtil.setGuestCookie(response, guestKey);
		}

		guestCartService.deleteCartForGuest(guestKey);

		request.getSession().setAttribute(CART_ITEMS_COUNTS, 0);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
