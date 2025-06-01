package com.nhnacademy.front.cart.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCartItemsForGuestDTO {

	private long productId;

	private String productTitle;

	private long productSalePrice;

	private String productImagePath;

	private int cartItemsQuantity;

	private long productTotalPrice;

}
