package com.nhnacademy.front.cart.adaptor;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.cart.model.dto.request.RequestAddCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForCustomerDTO;

@FeignClient(name = "customer-cart-adaptor", url = "${customer.url}")
public interface CustomerCartAdaptor {

	/**
	 * 비회원/회원 - 장바구니 목록 페이징 조회
	 */
	@GetMapping("/{customerId}/carts")
	ResponseEntity<List<ResponseCartItemsForCustomerDTO>> getCartItemsByCustomer(@PathVariable long customerId);

	/**
	 * 비회원/회원 - 장바구니 항목 추가
	 */
	@PostMapping("/carts/items")
	ResponseEntity<Void> createCartItemForCustomer(@RequestBody RequestAddCartItemsDTO requestDto);

	/**
	 * 비회원/회원 - 장바구니 항목 수량 변경
	 */
	@PutMapping("/carts/items/{cartItemsId}")
	ResponseEntity<Void> updateCartItemForCustomer(@PathVariable long cartItemsId, @RequestBody RequestUpdateCartItemsDTO requestDto);

	/**
	 * 비회원/회원 - 장바구니 항목 삭제
	 */
	@DeleteMapping("/carts/items/{cartItemId}")
	ResponseEntity<Void> deleteCartItemForCustomer(@PathVariable long cartItemId);

	/**
	 * 비회원/회원 - 장바구니 전체 삭제
	 */
	@DeleteMapping("/{customerId}/carts")
	ResponseEntity<Void> deleteCartForCustomer(@PathVariable long customerId);

}
