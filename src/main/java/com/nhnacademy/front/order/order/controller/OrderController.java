package com.nhnacademy.front.order.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
import com.nhnacademy.front.order.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class OrderController {
	@Value("${order.toss.client-key}")
	private String tossClientKey;

	@Value("${order.toss.success-url}")
	private String tossSuccessUrl;

	@Value("${order.toss.fail-url}")
	private String tossFailUrl;

	private final OrderService orderService;

	/**
	 * 결제 주문서 작성 페이지
	 */
	@GetMapping("/order")
	public String getCheckOut(Model model) {
		// 사용자가 주문하려는 상품 정보,쿠폰 내역, 포인트 정보 등을 보내줘야 함
		// 지금은 임시 값을 넣어 확인
		model.addAttribute("tossClientKey", tossClientKey);
		model.addAttribute("tossSuccessUrl", tossSuccessUrl);
		model.addAttribute("tossFailUrl", tossFailUrl);
		return "payment/checkout";
	}

	/**
	 * 결제하기 버튼을 눌렀을 때 back에 요청하여 주문서를 미리 저장하는 기능
	 */
	@PostMapping("/order/tossPay")
	public ResponseEntity<ResponseOrderResultDTO> postCheckOut(@Validated @RequestBody RequestOrderWrapperDTO request,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		return orderService.createOrder(request);
	}

	/**
	 * 결제하기 버튼을 눌렀을 때 back에 요청하여 주문서를 미리 저장하는 기능
	 */
	@PostMapping("/order/point")
	public ResponseEntity<ResponseOrderResultDTO> postPointCheckOut(
		@Validated @RequestBody RequestOrderWrapperDTO request,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		return orderService.createPointOrder(request);
	}

	/**
	 * 결제 완료 시 이동될 페이지
	 * 여기에서 토스에서 제공한 데이터를 back으로 요청하여 결제 승인을 진행
	 */
	@GetMapping("/order/success")
	public String getSuccessOrder(@RequestParam String orderId, @RequestParam String paymentKey,
		@RequestParam long amount) {
		// 결제 승인 요청
		ResponseEntity<Void> response = orderService.confirmOrder(orderId, paymentKey, amount);

		if (response.getStatusCode().is2xxSuccessful()) {
			// 결제 성공창으로 리다이렉트
			return "redirect:/order/confirm";
		} else {
			// 결제 실패 창으로 리다이렉트
			return "redirect:/order/fail";
		}
	}

	/**
	 * 외부 API의 경우 결제 승인까지 완료
	 * 포인트라면 결제 완료 시 이동하는 주문 완료 페이지
	 */
	@GetMapping("/order/confirm")
	public String getConfirmOrder() {
		// 추후 정보를 더 넣을지는 모름
		return "payment/confirmation";
	}

	@GetMapping("/order/fail")
	public String getFailOrder() {
		return "payment/fail";
	}

	@PostMapping("/order/cancel")
	public ResponseEntity<Void> cancelOrder(@RequestParam String orderId) {
		return orderService.cancelOrder(orderId);
	}
}
