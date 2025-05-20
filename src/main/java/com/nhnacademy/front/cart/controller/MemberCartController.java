package com.nhnacademy.front.cart.controller;

import java.util.List;
import java.util.Objects;

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

import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForMemberDTO;
import com.nhnacademy.front.cart.service.MemberCartService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberCartController {

	private final MemberCartService memberCartService;


	/**
	 * 회원 장바구니 항목 추가
	 */
	@JwtTokenCheck
	@PostMapping("/members/carts/items")
	public ResponseEntity<Void> memberAddToCart(HttpServletRequest request, @Validated @RequestBody RequestAddCartItemsDTO requestDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		if (Objects.isNull(memberId)) {
			throw new JwtException("JWT token is null");
		}

		requestDto.setMemberId(memberId);

		memberCartService.createCartItemForMember(requestDto);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 회원 장바구니 목록 조회
	 */
	@JwtTokenCheck
	@GetMapping("/members/carts")
	public String getCartsByMember(HttpServletRequest request, Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		if (Objects.isNull(memberId)) {
			throw new JwtException("JWT token is null");
		}

		List<ResponseCartItemsForMemberDTO> cartItemsByMember = memberCartService.getCartItemsByMember(memberId);

		long totalPaymentAmount = 0;
		for (ResponseCartItemsForMemberDTO cartItem : cartItemsByMember) {
			totalPaymentAmount += cartItem.getProductTotalPrice();
		}

		model.addAttribute("cartItemsByMember", cartItemsByMember);
		model.addAttribute("totalPaymentAmount", totalPaymentAmount);

		return "cart/member-cart";
	}

	/**
	 * 회원 장바구니 항목 수량 변경
	 */
	@JwtTokenCheck
	@PutMapping("/members/carts/items/{cartItemsId}")
	public ResponseEntity<Void> updateCartItemForMember(@PathVariable long cartItemsId, @Validated @RequestBody RequestUpdateCartItemsDTO requestDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		memberCartService.updateCartItemForMember(cartItemsId, requestDto);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 회원 장바구니 항목 삭제
	 */
	@JwtTokenCheck
	@DeleteMapping("/members/carts/items/{cartItemsId}")
	public ResponseEntity<Void> deleteCartItemForMember(@PathVariable long cartItemsId) {
		memberCartService.deleteCartItemForMember(cartItemsId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 회원 장바구니 전체 삭제
	 */
	@JwtTokenCheck
	@DeleteMapping("/members/carts")
	public ResponseEntity<Void> deleteCartForMember(HttpServletRequest request) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		if (Objects.isNull(memberId)) {
			throw new JwtException("JWT token is null");
		}

		memberCartService.deleteCartForMember(memberId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
