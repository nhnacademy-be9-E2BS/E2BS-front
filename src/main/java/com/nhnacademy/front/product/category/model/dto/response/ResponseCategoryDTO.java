package com.nhnacademy.front.product.category.model.dto.response;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseCategoryDTO {
	@NotNull
	private long categoryId;

	@NotNull
	private String categoryName;

	private List<ResponseCategoryDTO> children;
}
