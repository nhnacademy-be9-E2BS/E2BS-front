package com.nhnacademy.front.cart.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhnacademy.front.cart.model.dto.order.RequestCartOrderDTO;
import com.nhnacademy.front.cart.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;


	@PostMapping("/carts/order")
	public ResponseEntity<Map<String, String>> order(@RequestBody RequestCartOrderDTO requestDto) {
		log.info("dto: {}", requestDto.toString());
		return ResponseEntity.ok().body(Map.of("message", "success"));
	}

}
