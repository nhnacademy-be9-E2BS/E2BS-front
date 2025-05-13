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
public class RequestAddCartItemsDTO {

	private Long customerId;

	private String sessionId;

	@NotNull
	private long productId;

	@NotNull
	private int quantity;

}
