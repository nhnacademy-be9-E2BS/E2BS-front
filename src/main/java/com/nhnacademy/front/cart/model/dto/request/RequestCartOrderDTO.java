package com.nhnacademy.front.cart.model.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "선택한 장바구니 항목 주문 요청 DTO")
public class RequestCartOrderDTO {

	@NotNull
	@Schema(description = "상품 ID 리스트", example = "[1, 2, 3]", requiredMode = Schema.RequiredMode.REQUIRED)
	private List<Long> productIds;

	@NotNull
	@Schema(description = "상품별 수량 리스트 (productIds의 순서에 대응)", example = "[2, 1, 3]", requiredMode = Schema.RequiredMode.REQUIRED)
	private List<Integer> cartQuantities;

}
