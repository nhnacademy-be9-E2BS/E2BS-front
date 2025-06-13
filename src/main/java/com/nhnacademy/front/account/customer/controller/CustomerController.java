package com.nhnacademy.front.account.customer.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerLoginDTO;
import com.nhnacademy.front.account.customer.model.dto.request.RequestCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerDTO;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerRegisterDTO;
import com.nhnacademy.front.account.customer.service.CustomerService;
import com.nhnacademy.front.cart.model.dto.request.RequestCartOrderDTO;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.util.CookieUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Customer", description = "비회원 관련 API")
@Controller
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;
	private final ObjectMapper objectMapper;


	@Operation(summary = "회원 로그인 페이지", description = "회원 로그인 페이지를 반환합니다.")
	@GetMapping("/customers/login")
	public String getCustomerLogin() {
		return "member/login/customer-login";
	}

	@Operation(summary = "비회원 인증 폼 페이지", description = "비회원 인증을 위한 폼 페이지를 반환합니다.")
	@GetMapping("/order/auth")
	public String guestAuthForm() {
		return "customer/auth";
	}

	@Operation(summary = "비회원 장바구니 항목 쿠키 저장", description = "비회원이 주문할 장바구니 항목을 세션이 아닌 쿠키에 저장합니다.")
	@ApiResponse(responseCode = "200", description = "성공적으로 쿠키에 저장됨")
	@PostMapping("/order/auth")
	public ResponseEntity<Void> redirectGuestAuth(@Parameter(description = "장바구니 항목", required = true) @Valid @RequestBody RequestCartOrderDTO requestCartOrderDTO,
		                                          @Parameter(hidden = true) BindingResult bindingResult,
		                                          @Parameter(hidden = true) HttpServletResponse response) throws JsonProcessingException {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		String cartJson = objectMapper.writeValueAsString(requestCartOrderDTO);
		String encodedCart = Base64.getEncoder().encodeToString(cartJson.getBytes(StandardCharsets.UTF_8));

		CookieUtil.setCookie("orderCart", response, encodedCart);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "비회원 회원가입", description = "비회원이 주문 전 회원가입을 진행합니다.")
	@ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = ResponseCustomerRegisterDTO.class)))
	@PostMapping("/customers/register")
	public ResponseEntity<ResponseCustomerRegisterDTO> register(@Parameter(description = "회원가입 요청 DTO", required = true) @Validated @RequestBody RequestCustomerRegisterDTO requestCustomerRegisterDTO,
		                                                        @Parameter(hidden = true) BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		ResponseCustomerDTO customer = customerService.customerRegister(requestCustomerRegisterDTO);

		ResponseCustomerRegisterDTO response = new ResponseCustomerRegisterDTO(customer.getCustomerName(), customer.getCustomerId());
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "비회원 로그인 요청 (DTO 포함 응답)", description = "비회원이 장바구니 주문 전에 로그인합니다. 요청에 포함된 쿠키로 장바구니 정보를 복원합니다.")
	@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = ResponseCustomerRegisterDTO.class)))
	@PostMapping("/customers/login")
	public ResponseEntity<ResponseCustomerRegisterDTO> loginByCart(@Parameter(description = "비회원 로그인 요청 DTO", required = true) @Validated @RequestBody RequestCustomerLoginDTO requestCustomerLoginDTO,
		                                                           @Parameter(hidden = true) BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		ResponseCustomerDTO customer = customerService.customerLogin(requestCustomerLoginDTO);

		ResponseCustomerRegisterDTO response = new ResponseCustomerRegisterDTO(customer.getCustomerName(), customer.getCustomerId());
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "비회원 로그인 요청 (ID 반환)", description = "비회원이 로그인하여 고객 ID만 반환합니다.")
	@ApiResponse(responseCode = "200", description = "로그인 성공 및 고객 ID 반환", content = @Content(schema = @Schema(implementation = Long.class)))
	@PostMapping("/customers/order/login")
	public ResponseEntity<Long> login(@Parameter(description = "비회원 로그인 요청 DTO", required = true) @Validated @RequestBody RequestCustomerLoginDTO requestCustomerLoginDTO,
		                              @Parameter(hidden = true) BindingResult bindingResult) throws JsonProcessingException {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		ResponseCustomerDTO customer = customerService.customerLogin(requestCustomerLoginDTO);
		return ResponseEntity.ok(customer.getCustomerId());
	}

}
