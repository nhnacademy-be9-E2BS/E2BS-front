package com.nhnacademy.front.product.category.model;

import java.util.List;

import com.nhnacademy.front.product.category.model.dto.request.RequestCategoryDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RequestCategoryList {
	@Valid
	@Size(min = 2)
	@NotNull
	private List<RequestCategoryDTO> categories;
}
