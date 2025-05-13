package com.nhnacademy.front.cart.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCartItemsForGuestDTO {

	private long productId;

	private List<ProductCategoryDTO> categoryIds;

	private String productTitle;

	private long productSalePrice;

	private String productImagePath;

	private int cartItemsQuantity;

	private long productTotalPrice;

}
