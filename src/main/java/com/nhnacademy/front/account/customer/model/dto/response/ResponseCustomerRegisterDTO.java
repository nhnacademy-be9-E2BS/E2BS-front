package com.nhnacademy.front.account.customer.model.dto.response;

import com.nhnacademy.front.cart.model.dto.request.RequestCartOrderDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비회원 가입 응답 DTO")
public class ResponseCustomerRegisterDTO {

	@Schema(description = "고객 이름", example = "홍길동")
	private String customerName;

	@Schema(description = "고객 ID", example = "1001")
	private long customerId;

	@Schema(description = "비회원 장바구니 주문 정보")
	private RequestCartOrderDTO requestCartOrder;

}
