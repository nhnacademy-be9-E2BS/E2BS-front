package com.nhnacademy.front.cart.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.cart.model.dto.ResponseCartItemsForCustomerDTO;
import com.nhnacademy.front.cart.service.CustomerCartService;
import com.nhnacademy.front.cart.service.GuestCartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

	private final MemberService memberService;
	private final CustomerCartService customerCartService;
	private final GuestCartService guestCartService;


	@GetMapping("/customers/carts")
	public String customerCartsPage(Model model) {
		// memberService.findByCustomerId();getCartItemsByCustomer
		List<ResponseCartItemsForCustomerDTO> cartItemsByCustomer = customerCartService.getCartItemsByCustomer(3L);

		long totalPaymentAmount = 0;
		for (ResponseCartItemsForCustomerDTO cartItem : cartItemsByCustomer) {
			totalPaymentAmount += cartItem.getProductTotalPrice();
		}

		model.addAttribute("cartItemsByCustomer", cartItemsByCustomer);
		model.addAttribute("totalPaymentAmount", totalPaymentAmount);

		return "cart/customer-cart";
	}


}
