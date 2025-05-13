package com.nhnacademy.front.cart.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateCartItemsDTO {
	private String sessionId;
	private Long productId;
	@NotNull
	private int quantity;
}
