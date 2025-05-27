package com.nhnacademy.front.product.state.model.dto.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ProductStateName {
	SALE, OUT, DELETE, END
}
