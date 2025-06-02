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
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartItemsForGuestDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestUpdateCartItemsDTO;
import com.nhnacademy.front.cart.model.dto.response.ResponseCartItemsForGuestDTO;

@FeignClient(name = "guest-cart-adaptor", url = "${guest.url}")
public interface GuestCartAdaptor {

	@GetMapping("/{sessionId}/carts")
	ResponseEntity<List<ResponseCartItemsForGuestDTO>> getCartItemsByGuest(@PathVariable String sessionId);

	@PostMapping("/carts/items")
	ResponseEntity<Integer> createCartItemForGuest(@RequestBody RequestAddCartItemsDTO requestDto);

	@PutMapping("/carts/items")
	ResponseEntity<Integer> updateCartItemForGuest(@RequestBody RequestUpdateCartItemsDTO requestDto);

	@DeleteMapping("/carts/items")
	ResponseEntity<Void> deleteCartItemForGuest(@RequestBody RequestDeleteCartItemsForGuestDTO requestDto);

	@DeleteMapping("/{sessionId}/carts")
	ResponseEntity<Void> deleteCartForGuest(@PathVariable String sessionId);

}
