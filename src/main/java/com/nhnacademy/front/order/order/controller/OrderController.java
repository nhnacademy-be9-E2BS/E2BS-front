package com.nhnacademy.front.order.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.account.address.model.dto.response.ResponseMemberAddressDTO;
import com.nhnacademy.front.account.address.service.AddressService;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.cart.model.dto.order.RequestCartOrderDTO;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseOrderCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.service.MemberCouponService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDetailDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;
import com.nhnacademy.front.order.order.service.OrderService;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;
import com.nhnacademy.front.order.wrapper.service.WrapperService;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryIdsDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductAdminService;
import com.nhnacademy.front.product.product.service.ProductService;

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

	private final ProductService productService;
	private final WrapperService wrapperService;
	private final MemberMypageService memberMypageService;
	private final AddressService addressService;

	private final UserCategoryService userCategoryService;
	private final MemberCouponService memberCouponService;
	/**
	 * 결제 주문서 작성 페이지
	 */
	@JwtTokenCheck
	@PostMapping("/order")
	public String getCheckOut(Model model, @ModelAttribute RequestCartOrderDTO orderRequest, HttpServletRequest request) {
		List<Integer> quantities = orderRequest.getCartQuantities();
		List<ResponseProductReadDTO> products = productService.getProducts(orderRequest.getProductIds());
		List<ResponseWrapperDTO> wrappers = wrapperService.getWrappersBySaleable(Pageable.unpaged()).getContent();
		ResponseMemberInfoDTO member = memberMypageService.getMemberInfo(request);
		long memberPoint = memberMypageService.getMemberPoint(new RequestMemberIdDTO(member.getMemberId()));
		List<ResponseMemberAddressDTO> addresses = addressService.getMemberAddresses(member.getMemberId());
		ResponseMemberAddressDTO defaultAddress = addresses.stream().filter(addr -> addr.isAddressDefault()).findFirst().orElse(null);

		List<ResponseOrderCouponDTO> coupons = memberCouponService.getCouponsInOrder(member.getMemberId(),orderRequest.getProductIds());
		List<ResponseCategoryIdsDTO> categories = userCategoryService.getCategoriesByProductIds(orderRequest.getProductIds());
		Map<Long, List<Long>> productCategoryMap = new HashMap<>();
		for (ResponseCategoryIdsDTO dto : categories) {
			productCategoryMap.put(dto.getProductId(), dto.getCategoryIds());
		}

		model.addAttribute("products", products);
		model.addAttribute("quantities", quantities);
		model.addAttribute("wrappers", wrappers);
		model.addAttribute("member", member);
		model.addAttribute("memberPoint", memberPoint);
		model.addAttribute("addresses", addresses);
		model.addAttribute("defaultAddress", defaultAddress);
		model.addAttribute("productCategories", productCategoryMap);
		model.addAttribute("coupons", coupons);
		model.addAttribute("deliveryFee", deliveryFeeSevice.getCurrentDeliveryFee());
		model.addAttribute("tossClientKey", tossClientKey);
		model.addAttribute("tossSuccessUrl", tossSuccessUrl);
		model.addAttribute("tossFailUrl", tossFailUrl);
		return "payment/checkout";
	}

	/**
	 * 테스트 용 데이터
	 */
	@JwtTokenCheck
	@GetMapping("/order")
	public String getCheckOutTest(Model model) {
		// 사용자가 주문하려는 상품 정보,쿠폰 내역, 포인트 정보 등을 보내줘야 함
		// 지금은 임시 값을 넣어 확인
		model.addAttribute("deliveryFee", deliveryFeeSevice.getCurrentDeliveryFee());
		model.addAttribute("tossClientKey", tossClientKey);
		model.addAttribute("tossSuccessUrl", tossSuccessUrl);
		model.addAttribute("tossFailUrl", tossFailUrl);

		/**
		 * 쿠폰: 주문서에서 담긴 상품에 적용가능한 쿠폰 리스트
		 * memberId : JWT 토큰에서 꺼내기
		 * RequestCartOrderDTO : 장바구니-모두구매 or 도서상세페이지-구매하기 누르면 상품 ID 리스트가 여기에 담김
		 * List<ResponseOrderCouponDTO> response = memberCouponService.getCouponsInOrder(String memberId, RequestCartOrderDTO request);
		 */

		/**
		 * 포인트: 사용자가 보유한 포인트 조회
		 * MemberMyPageService.getMemberPoint() 쓰면 될듯
		 */

		return "payment/checkoutTest";
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
	 * 포인트 결제하기 버튼을 눌렀을 때 back에 요청하여 주문서를 저장 및 결제 처리 기능
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
			// 외부 API 결제 승인 실패 시 저장된 주문서를 제거하도록 요청
			orderService.deleteOrder(orderId);
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

	/**
	 * 주문 실패 시 이동할 페이지
	 */
	@GetMapping("/order/fail")
	public String getFailOrder() {
		return "payment/fail";
	}

	/**
	 * 결제 모달을 끌 시 호출 할 주문서 삭제 요청
	 */
	@JwtTokenCheck
	@PostMapping("/order/cancel")
	public ResponseEntity<Void> deleteOrder(@RequestParam String orderId) {
		return orderService.deleteOrder(orderId);
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

	/**
	 * 회원이 자신의 주문 특정 내역 조회
	 */
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

	/**
	 * 회원이 배송 시작 전인 특정 주문에 대해 주문을 취소하는 기능
	 */
	@JwtTokenCheck
	@DeleteMapping("/mypage/orders/{orderCode}")
	public ResponseEntity<Void> cancelOrder(@PathVariable String orderCode) {
		return orderService.cancelOrder(orderCode);
	}
}
