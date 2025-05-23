package com.nhnacademy.front.cart.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO implements Serializable {
	private List<CartItemDTO> cartItems = new ArrayList<>();
}
