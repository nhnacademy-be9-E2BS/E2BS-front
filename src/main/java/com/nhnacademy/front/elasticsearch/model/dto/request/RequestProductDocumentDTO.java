package com.nhnacademy.front.elasticsearch.model.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestProductDocumentDTO {
	@NotNull
	private Long productId;

	@NotNull
	private String productTitle;

	@NotNull
	private String productContent;

	@NotNull
	private String productPublisherName;

	@NotNull
	private LocalDate productPublishedAt;

	@NotNull
	private Long productSalePrice;

	@NotNull
	private List<String> productContributors;

	private List<String> productTags;

	@NotNull
	private List<Long> productCategoryIds;

}
