package com.nhnacademy.front.order.wrapper.model.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseWrapperDTO {
	@NotNull
	private long wrapperId;
	@NotNull
	private long wrapperPrice;
	@NotNull
	private String wrapperName;
	@NotNull
	private String wrapperImage;
	@NotNull
	private boolean wrapperSaleable;
}
