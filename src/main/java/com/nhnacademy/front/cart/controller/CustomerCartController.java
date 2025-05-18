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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForCustomerDTO;
import com.nhnacademy.front.cart.service.CustomerCartService;
import com.nhnacademy.front.common.exception.ValidationFailedException;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CustomerCartController {

	private final MemberService memberService;
	private final CustomerCartService customerCartService;


	/**
	 * 고객 장바구니 항목 추가
	 */
	@PostMapping("/customers/carts/items")
	public ResponseEntity<Void> customerAddToCart(@Validated @RequestBody RequestAddCartItemsDTO requestDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		requestDto.setCustomerId(1L);
		customerCartService.createCartItemForCustomer(requestDto);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 고객 장바구니 목록 조회
	 */
	@GetMapping("/customers/carts")
	public String getCartsByCustomer(Model model) {
		// 용경: 고객 ID를 받아야함
		List<ResponseCartItemsForCustomerDTO> cartItemsByCustomer = customerCartService.getCartItemsByCustomer(1L);

		long totalPaymentAmount = 0;
		for (ResponseCartItemsForCustomerDTO cartItem : cartItemsByCustomer) {
			totalPaymentAmount += cartItem.getProductTotalPrice();
		}

		model.addAttribute("cartItemsByCustomer", cartItemsByCustomer);
		model.addAttribute("totalPaymentAmount", totalPaymentAmount);

		return "cart/customer-cart";
	}

	/**
	 * 고객 장바구니 항목 수량 변경
	 */
	@PutMapping("/customers/carts/items/{cartItemsId}")
	public ResponseEntity<Void> updateCartItemForCustomer(@PathVariable long cartItemsId, @Validated @RequestBody RequestUpdateCartItemsDTO requestDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		customerCartService.updateCartItemForCustomer(cartItemsId, requestDto);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 고객 장바구니 항목 삭제
	 */
	@DeleteMapping("/customers/carts/items/{cartItemsId}")
	public ResponseEntity<Void> deleteCartItemForCustomer(@PathVariable long cartItemsId) {
		customerCartService.deleteCartItemForCustomer(cartItemsId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 고객 장바구니 전체 삭제
	 */
	@DeleteMapping("/customers/{customerId}/carts")
	public ResponseEntity<Void> deleteCartForCustomer(@PathVariable long customerId) {
		customerCartService.deleteCartForCustomer(customerId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
