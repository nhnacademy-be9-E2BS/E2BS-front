package com.nhnacademy.front.cart.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCartCountDTO {

	private String memberId;

	private String sessionId;

}
