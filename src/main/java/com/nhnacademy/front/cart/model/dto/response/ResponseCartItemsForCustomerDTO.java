package com.nhnacademy.front.cart.model.dto.response;

import java.util.List;

import com.nhnacademy.front.cart.model.dto.ProductCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCartItemsForCustomerDTO {

	private long cartItemId;

	private long productId;

	private List<ProductCategoryDTO> categoryIds;

	private String productTitle;

	private long productSalePrice;

	private String productImagePath;

	private int cartItemsQuantity;

	private long productTotalPrice;

}
