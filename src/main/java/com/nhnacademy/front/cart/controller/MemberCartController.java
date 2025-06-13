package com.nhnacademy.front.cart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "MemberCart", description = "회원 장바구니 관련 API")
@Controller
@RequiredArgsConstructor
public class MemberCartController {

	private final MemberCartService memberCartService;
	private final DeliveryFeeSevice deliveryFeeSevice;
	private static final String CART_ITEMS_COUNTS = "cartItemsCounts";

	@JwtTokenCheck
	@Operation(summary = "회원 장바구니 항목 추가",
		description = "회원 장바구니에 상품을 추가합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "장바구니에 항목 추가 성공", content = @Content(schema = @Schema(implementation = Integer.class))),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@PostMapping("/members/carts/items")
	public ResponseEntity<Integer> memberAddToCart(
		@Parameter(description = "추가할 장바구니 항목 정보", required = true) @Valid @RequestBody RequestAddCartItemsDTO requestDto,
		@Parameter(hidden = true) BindingResult bindingResult,
		@Parameter(hidden = true) HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		requestDto.setMemberId(memberId);

		int cartItemsCounts = memberCartService.createCartItemForMember(requestDto);
		request.getSession().setAttribute(CART_ITEMS_COUNTS, cartItemsCounts);

		return ResponseEntity.status(HttpStatus.CREATED).body(cartItemsCounts);
	}

	@JwtTokenCheck
	@Operation(summary = "회원 장바구니 목록 조회",
		description = "회원의 장바구니에 담긴 상품 목록을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "장바구니 목록 조회 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@GetMapping("/members/carts")
	public String getCartsByMember(@Parameter(hidden = true) HttpServletRequest request,
		@Parameter(hidden = true) Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		List<ResponseCartItemsForMemberDTO> cartItemsByMember = memberCartService.getCartItemsByMember(memberId);

		long totalProductPrice = 0;
		for (ResponseCartItemsForMemberDTO cartItem : cartItemsByMember) {
			totalProductPrice += cartItem.getProductTotalPrice();
		}

		long currentDeliveryPrice = 0;
		ResponseDeliveryFeeDTO currentDeliveryFee = deliveryFeeSevice.getCurrentDeliveryFee();
		if (totalProductPrice < currentDeliveryFee.getDeliveryFeeFreeAmount()) {
			currentDeliveryPrice = currentDeliveryFee.getDeliveryFeeAmount();
		}

		model.addAttribute("cartItemsByMember", cartItemsByMember);
		model.addAttribute("totalProductPrice", totalProductPrice);
		model.addAttribute("currentDeliveryPrice", currentDeliveryPrice);
		model.addAttribute("currentDeliveryFee", currentDeliveryFee);

		return "cart/member-cart";
	}

	@JwtTokenCheck
	@Operation(summary = "회원 장바구니 항목 수량 변경",
		description = "회원 장바구니의 특정 항목 수량을 변경합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "수량 변경 성공", content = @Content(schema = @Schema(implementation = Integer.class))),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패"),
			@ApiResponse(responseCode = "404", description = "해당 장바구니 항목 없음")
		})
	@PutMapping("/members/carts/items/{cartItemsId}")
	public ResponseEntity<Integer> updateCartItemForMember(
		@Parameter(description = "장바구니 항목 ID", required = true) @PathVariable long cartItemsId,
		@Parameter(description = "변경할 장바구니 항목 정보", required = true) @Valid @RequestBody RequestUpdateCartItemsDTO requestDto,
		@Parameter(hidden = true) BindingResult bindingResult,
		@Parameter(hidden = true) HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		requestDto.setMemberId(memberId);

		int cartItemsCounts = memberCartService.updateCartItemForMember(cartItemsId, requestDto);
		request.getSession().setAttribute(CART_ITEMS_COUNTS, cartItemsCounts);

		return ResponseEntity.ok(cartItemsCounts);
	}

	@JwtTokenCheck
	@Operation(summary = "회원 장바구니 항목 삭제",
		description = "회원 장바구니에서 특정 항목을 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "항목 삭제 성공", content = @Content(schema = @Schema(implementation = Integer.class))),
			@ApiResponse(responseCode = "401", description = "인증 실패"),
			@ApiResponse(responseCode = "404", description = "해당 항목 없음")
		})
	@DeleteMapping("/members/carts/items/{cartItemsId}")
	public ResponseEntity<Integer> deleteCartItemForMember(
		@Parameter(description = "장바구니 항목 ID", required = true) @PathVariable long cartItemsId,
		@Parameter(hidden = true) HttpServletRequest request) {
		memberCartService.deleteCartItemForMember(cartItemsId);

		HttpSession session = request.getSession();
		Integer cartItemsCounts = (Integer)session.getAttribute(CART_ITEMS_COUNTS);
		int updatedCount = cartItemsCounts != null ? cartItemsCounts - 1 : 0;
		session.setAttribute(CART_ITEMS_COUNTS, updatedCount);

		return ResponseEntity.ok(updatedCount);
	}

	@JwtTokenCheck
	@Operation(summary = "회원 장바구니 전체 삭제",
		description = "회원 장바구니에 담긴 모든 항목을 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "204", description = "전체 삭제 성공"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
		})
	@DeleteMapping("/members/carts")
	public ResponseEntity<Void> deleteCartForMember(@Parameter(hidden = true) HttpServletRequest request) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		memberCartService.deleteCartForMember(memberId);
		request.getSession().setAttribute(CART_ITEMS_COUNTS, 0);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
