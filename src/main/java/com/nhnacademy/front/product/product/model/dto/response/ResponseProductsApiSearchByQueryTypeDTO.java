package com.nhnacademy.front.product.product.model.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductsApiSearchByQueryTypeDTO {
	private String publisherName;
	private String productTitle;
	private String productIsbn;
	private String productImage;
	private String productDescription;
	private long productRegularPrice;
	private long productSalePrice;
	private String contributors;
	private LocalDate productPublishedAt;
}
