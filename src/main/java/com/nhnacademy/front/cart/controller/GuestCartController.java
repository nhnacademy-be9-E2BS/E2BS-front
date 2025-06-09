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
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.util.GuestCookieUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "GuestCart", description = "게스트 장바구니 관련 API")
@Controller
@RequiredArgsConstructor
public class GuestCartController {

	private final GuestCartService guestCartService;
	private static final String CART_ITEMS_COUNTS = "cartItemsCounts";


	@Operation(summary = "게스트 장바구니 항목 추가", description = "게스트 장바구니에 상품을 추가합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "장바구니 추가 성공", content = @Content(schema = @Schema(implementation = Integer.class))),
		@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
	})
	@PostMapping("/guests/carts/items")
	public ResponseEntity<Integer> guestAddToCart(@Parameter(description = "추가할 장바구니 항목 정보", required = true) @Validated @RequestBody RequestAddCartItemsDTO requestDto,
		                                          @Parameter(hidden = true) BindingResult bindingResult,
		                                          @Parameter(hidden = true) HttpServletRequest request,
		                                          @Parameter(hidden = true) HttpServletResponse response) {

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

	@Operation(summary = "게스트 장바구니 목록 조회", description = "게스트의 장바구니에 담긴 모든 항목을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "장바구니 조회 성공")
	@GetMapping("/guests/carts")
	public String getCartsByGuest(@Parameter(hidden = true) HttpServletRequest request, @Parameter(hidden = true) HttpServletResponse response,
		                          @Parameter(hidden = true) Model model) {
		String guestKey = GuestCookieUtil.getGuestKey(request);
		if (Objects.isNull(guestKey)) {
			guestKey = UUID.randomUUID().toString();
			GuestCookieUtil.setGuestCookie(response, guestKey);
		}

		List<ResponseCartItemsForGuestDTO> cartItemsByGuest = guestCartService.getCartItemsByGuest(guestKey);

		long totalProductPrice = 0;
		long totalDeliveryPrice = 0;
		for (ResponseCartItemsForGuestDTO cartItem : cartItemsByGuest) {
			totalProductPrice += cartItem.getProductTotalPrice();
			totalDeliveryPrice += cartItem.getDeliveryFee().getDeliveryFeeAmount();
		}

		model.addAttribute("cartItemsByGuest", cartItemsByGuest);
		model.addAttribute("totalProductPrice", totalProductPrice);
		model.addAttribute("totalDeliveryPrice", totalDeliveryPrice);

		return "cart/guest-cart";
	}

	@Operation(summary = "게스트 장바구니 항목 수량 변경", description = "게스트 장바구니에 담긴 항목의 수량을 변경합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "수량 변경 성공", content = @Content(schema = @Schema(implementation = Integer.class))),
		@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
	})
	@PutMapping("/guests/carts/items")
	public ResponseEntity<Integer> updateCartItemForGuest(@Parameter(description = "수정할 장바구니 항목 정보", required = true) @Validated @RequestBody RequestUpdateCartItemsDTO requestDto,
		                                                  @Parameter(hidden = true) BindingResult bindingResult,
		                                                  @Parameter(hidden = true) HttpServletRequest request,
		                                                  @Parameter(hidden = true) HttpServletResponse response) {
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

	@Operation(summary = "게스트 장바구니 항목 삭제", description = "게스트 장바구니에서 특정 항목을 삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "항목 삭제 성공", content = @Content(schema = @Schema(implementation = Integer.class))),
		@ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
	})
	@DeleteMapping("/guests/carts/items")
	public ResponseEntity<Integer> deleteCartItemForGuest(@Parameter(description = "삭제할 장바구니 항목 정보", required = true) @Validated @RequestBody RequestDeleteCartItemsForGuestDTO requestDto,
		                                                  @Parameter(hidden = true) BindingResult bindingResult,
		                                                  @Parameter(hidden = true) HttpServletRequest request,
		                                                  @Parameter(hidden = true) HttpServletResponse response) {
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
		request.getSession().setAttribute(CART_ITEMS_COUNTS, cartItemsCounts - 1);

		return ResponseEntity.ok(cartItemsCounts - 1);
	}

	@Operation(summary = "게스트 장바구니 전체 삭제", description = "게스트 장바구니에 담긴 모든 항목을 삭제합니다.")
	@ApiResponse(responseCode = "204", description = "전체 항목 삭제 성공")
	@DeleteMapping("/guests/carts")
	public ResponseEntity<Void> deleteCartForGuest(@Parameter(hidden = true) HttpServletRequest request,
		                                           @Parameter(hidden = true) HttpServletResponse response) {
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