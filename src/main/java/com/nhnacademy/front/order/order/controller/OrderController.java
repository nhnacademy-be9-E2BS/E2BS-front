package com.nhnacademy.front.order.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDetailDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;
import com.nhnacademy.front.order.order.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
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
	private final DeliveryFeeSevice deliveryFeeSevice;

	/**
	 * 결제 주문서 작성 페이지
	 */
	@JwtTokenCheck
	@GetMapping("/order")
	public String getCheckOut(Model model) {
		// 사용자가 주문하려는 상품 정보,쿠폰 내역, 포인트 정보 등을 보내줘야 함
		// 지금은 임시 값을 넣어 확인
		model.addAttribute("deliveryFee", deliveryFeeSevice.getCurrentDeliveryFee());
		model.addAttribute("tossClientKey", tossClientKey);
		model.addAttribute("tossSuccessUrl", tossSuccessUrl);
		model.addAttribute("tossFailUrl", tossFailUrl);
		return "payment/checkout";
	}

	/**
	 * 결제하기 버튼을 눌렀을 때 back에 요청하여 주문서를 미리 저장하는 기능
	 */
	@JwtTokenCheck
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
	@JwtTokenCheck
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
	@JwtTokenCheck
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
	@JwtTokenCheck
	@GetMapping("/order/confirm")
	public String getConfirmOrder() {
		// 추후 정보를 더 넣을지는 모름
		return "payment/confirmation";
	}

	/**
	 * 주문 실패 시 이동할 페이지
	 */
	@JwtTokenCheck
	@GetMapping("/order/fail")
	public String getFailOrder() {
		return "payment/fail";
	}

	/**
	 * 결제 모달을 끌 시 호출 할 주문서 삭제 요청
	 */
	@JwtTokenCheck
	@PostMapping("/order/cancel")
	public ResponseEntity<Void> cancelOrder(@RequestParam String orderId) {
		return orderService.cancelOrder(orderId);
	}

	@JwtTokenCheck
	@GetMapping("/mypage/orders")
	public String getMemberOrders(Model model, HttpServletRequest request,
		@PageableDefault(page = 0, size = 10) Pageable pageable) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		ResponseEntity<PageResponse<ResponseOrderDTO>> response = orderService.getOrdersByMemberId(pageable, memberId);
		PageResponse<ResponseOrderDTO> pageResponse = response.getBody();
		Page<ResponseOrderDTO> orders = PageResponseConverter.toPage(pageResponse);
		model.addAttribute("orders", orders);
		return "member/mypage/orders";
	}

	@JwtTokenCheck
	@GetMapping("/mypage/orders/{orderCode}")
	public String getMemberOrderDetails(Model model, @PathVariable String orderCode) {
		ResponseEntity<ResponseOrderWrapperDTO> response = orderService.getOrderByOrderCode(orderCode);
		ResponseOrderWrapperDTO responseOrder = response.getBody();
		ResponseOrderDTO order = responseOrder.getOrder();
		List<ResponseOrderDetailDTO> orderDetails = responseOrder.getOrderDetails();

		long productAmount = 0;
		for (ResponseOrderDetailDTO orderDetail : orderDetails) {
			productAmount += orderDetail.getOrderDetailPerPrice() * orderDetail.getOrderQuantity();
			// 포장지가 있다면 포장지 가격도 포함
			if (orderDetail.getWrapperPrice() != null) {
				productAmount += orderDetail.getWrapperPrice() * orderDetail.getOrderQuantity();
			}
		}

		model.addAttribute("order", order);
		model.addAttribute("orderDetails", orderDetails);
		model.addAttribute("productAmount", productAmount);

		return "member/mypage/orderDetails";
	}
}
