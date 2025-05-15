package com.nhnacademy.front.order.wrapper.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RequestModifyWrapperDTO {
	@NotNull
	private Boolean wrapperSaleable;
}
