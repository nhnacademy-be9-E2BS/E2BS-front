package com.nhnacademy.front.product.product.model.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestProductApiCreateDTO {
	private String publisherName;

	private String productTitle;

	private String productIsbn;

	private String productImage;

	private String productDescription;

	private long productRegularPrice;

	private long productSalePrice;

	private String contributors;

	private String productContent;

	private boolean productPackageable;

	private int productStock;

	List<Long> categoryIds;
	List<Long> tagIds;
}
