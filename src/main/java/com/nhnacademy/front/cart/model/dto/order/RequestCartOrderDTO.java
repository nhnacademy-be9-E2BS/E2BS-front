package com.nhnacademy.front.cart.model.dto.order;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCartOrderDTO {
	private List<Long> productIds;
	private List<Integer> cartQuantities;
}
