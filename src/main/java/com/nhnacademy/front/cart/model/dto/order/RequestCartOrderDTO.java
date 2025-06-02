package com.nhnacademy.front.cart.model.dto.order;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCartOrderDTO {

	@NotNull
	private List<Long> productIds;

	@NotNull
	private List<Integer> cartQuantities;

}
