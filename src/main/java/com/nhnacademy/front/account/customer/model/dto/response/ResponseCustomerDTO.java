package com.nhnacademy.front.account.customer.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비회원 응답 DTO")
public class ResponseCustomerDTO {

	@Schema(description = "고객 이름", example = "홍길동")
	private String customerName;

	@Schema(description = "고객 ID", example = "1001")
	private long customerId;

}
