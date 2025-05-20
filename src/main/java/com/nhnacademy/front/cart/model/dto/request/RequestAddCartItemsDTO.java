package com.nhnacademy.front.cart.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAddCartItemsDTO {

	private String memberId;

	private String sessionId;

	@NotNull
	private long productId;

	/// 단일로만 담게 되는 경우가 있어 front단에서는 null제약이 필요 없음
 	private Integer quantity;

}
