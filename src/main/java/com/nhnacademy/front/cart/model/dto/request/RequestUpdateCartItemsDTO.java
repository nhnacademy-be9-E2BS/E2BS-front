package com.nhnacademy.front.cart.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateCartItemsDTO {
	private String sessionId;
	private Long productId;
	@NotNull
	private int quantity;
}
