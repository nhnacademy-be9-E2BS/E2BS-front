package com.nhnacademy.front.order.order.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.account.address.model.dto.response.ResponseMemberAddressDTO;
import com.nhnacademy.front.account.address.service.AddressService;
import com.nhnacademy.front.account.customer.model.dto.response.ResponseCustomerRegisterDTO;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.cart.model.dto.request.RequestCartOrderDTO;
import com.nhnacademy.front.cart.model.dto.request.RequestDeleteCartOrderDTO;
import com.nhnacademy.front.cart.service.CartService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.common.util.CookieUtil;
import com.nhnacademy.front.coupon.membercoupon.model.dto.response.ResponseOrderCouponDTO;
import com.nhnacademy.front.coupon.membercoupon.service.MemberCouponService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.jwt.parser.JwtHasToken;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderReturnDTO;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderWrapperDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderDetailDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderResultDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderReturnDTO;
import com.nhnacademy.front.order.order.model.dto.response.ResponseOrderWrapperDTO;
import com.nhnacademy.front.order.order.service.OrderService;
import com.nhnacademy.front.order.wrapper.model.dto.response.ResponseWrapperDTO;
import com.nhnacademy.front.order.wrapper.service.WrapperService;
import com.nhnacademy.front.product.category.model.dto.response.ResponseCategoryIdsDTO;
import com.nhnacademy.front.product.category.service.UserCategoryService;
import com.nhnacademy.front.product.product.model.dto.response.ResponseProductReadDTO;
import com.nhnacademy.front.product.product.service.ProductService;
import com.nhnacademy.front.review.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "비회원, 회원 주문 기능", description = "비회원, 회원 고객의 주문, 마이 페이지 주문 관련 기능 제공")
@Slf4j
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
	private final CartService cartService;
	private final ReviewService reviewService;

	private final ObjectMapper objectMapper;

	private static final String CART_ITEMS_COUNTS = "cartItemsCounts";


	/**
	 * 회원 결제 주문서 작성 페이지
	 */
	@Operation(summary = "회원 주문서 작성 페이지", description = "회원이 상품 바로구매, 장바구니 주문하기 시 주문서 페이지 제공")
	@JwtTokenCheck
	@GetMapping("/members/order")
	public String getCheckOut(Model model,
		@Parameter(description = "주문 상품 정보")
		@CookieValue(name = "orderCart") String encodedCart,
		HttpServletRequest request) throws JsonProcessingException {

		// Base64 디코딩 → JSON → DTO 역직렬화
		String orderCartJson = new String(Base64.getDecoder().decode(encodedCart), StandardCharsets.UTF_8);
		RequestCartOrderDTO orderRequest = objectMapper.readValue(orderCartJson, RequestCartOrderDTO.class);

		List<Integer> quantities = orderRequest.getCartQuantities();
		List<ResponseProductReadDTO> products = productService.getProducts(orderRequest.getProductIds());
		List<ResponseWrapperDTO> wrappers = wrapperService.getWrappersBySaleable(Pageable.unpaged()).getContent();
		ResponseMemberInfoDTO member = memberMypageService.getMemberInfo(request);
		long memberPoint = memberMypageService.getMemberPoint(new RequestMemberIdDTO(member.getMemberId()));
		List<ResponseMemberAddressDTO> addresses = addressService.getMemberAddresses(member.getMemberId());
		ResponseMemberAddressDTO defaultAddress = addresses.stream()
			.filter(addr -> addr.isAddressDefault())
			.findFirst()
			.orElse(null);

		List<ResponseOrderCouponDTO> coupons = memberCouponService.getCouponsInOrder(member.getMemberId(),
			orderRequest.getProductIds());
		List<ResponseCategoryIdsDTO> categories = userCategoryService.getCategoriesByProductIds(
			orderRequest.getProductIds());
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
		return "payment/member-checkout";
	}

	/**
	 * 비회원 가입 또는 로그인시 선택한 장바구니 항목들에 대한 결제 주문서 작성 페이지
	 */
	@Operation(summary = "비회원 주문서 작성 페이지", description = "비회원이 상품 바로구매, 장바구니 주문하기 시 주문서 페이지 제공")
	@PostMapping("/customers/order")
	public String getCheckOutCustomerCart(@Parameter(description = "주문 상품 정보") @CookieValue(name = "orderCart") String encodedCart, Model model,
		                                  @Parameter(description = "고객 정보", required = true) @ModelAttribute ResponseCustomerRegisterDTO customerRequest)
		                                  throws JsonProcessingException {
		// 회원 결제에서 쿠폰, 포인트, 주소만 제외
		String orderCartJson = new String(Base64.getDecoder().decode(encodedCart), StandardCharsets.UTF_8);
		RequestCartOrderDTO orderRequest = objectMapper.readValue(orderCartJson, RequestCartOrderDTO.class);

		List<Integer> quantities = orderRequest.getCartQuantities();
		List<ResponseProductReadDTO> products = productService.getProducts(orderRequest.getProductIds());
		List<ResponseWrapperDTO> wrappers = wrapperService.getWrappersBySaleable(Pageable.unpaged()).getContent();

		List<ResponseCategoryIdsDTO> categories = userCategoryService.getCategoriesByProductIds(orderRequest.getProductIds());
		Map<Long, List<Long>> productCategoryMap = new HashMap<>();
		for (ResponseCategoryIdsDTO dto : categories) {
			productCategoryMap.put(dto.getProductId(), dto.getCategoryIds());
		}

		model.addAttribute("products", products);
		model.addAttribute("quantities", quantities);
		model.addAttribute("wrappers", wrappers);
		model.addAttribute("customerId", customerRequest.getCustomerId());
		model.addAttribute("customerName", customerRequest.getCustomerName());
		model.addAttribute("productCategories", productCategoryMap);
		model.addAttribute("deliveryFee", deliveryFeeSevice.getCurrentDeliveryFee());
		model.addAttribute("tossClientKey", tossClientKey);
		model.addAttribute("tossSuccessUrl", tossSuccessUrl);
		model.addAttribute("tossFailUrl", tossFailUrl);
		return "payment/customer-checkout";
	}

	/**
	 * 결제하기 버튼을 눌렀을 때 back에 요청하여 주문서를 미리 저장하는 기능
	 */
	@Operation(summary = "외부 API 결제 요청 시 주문서 저장", description = "외부 API 결제 요청 시 DB에 주문 정보를 저장")
	@PostMapping("/order/tossPay")
	public ResponseEntity<ResponseOrderResultDTO> postCheckOut(
		@Parameter(description = "주문 상품 정보") @Validated @RequestBody RequestOrderWrapperDTO request,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		return orderService.createOrder(request);
	}

	/**
	 * 포인트로만 결제 시 back에 요청하여 주문서를 저장 및 결제 처리 기능
	 */
	@Operation(summary = "포인트 결제 처리 ", description = "포인트로만 전체 결제를 진행할 때 주문 정보 저장, 결제 처리")
	@JwtTokenCheck
	@PostMapping("/order/point")
	public ResponseEntity<ResponseOrderResultDTO> postPointCheckOut(
		@Parameter(description = "주문 상품 정보") @Validated @RequestBody RequestOrderWrapperDTO request,
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
	@Operation(summary = "결제 승인 처리", description = "주문 완료 시 페이지 제공")
	@GetMapping("/order/success")
	public String getSuccessOrder(
		@Parameter(description = "주문 코드") @RequestParam String orderId,
		@Parameter(description = "외부 API에서 발급된 결제 키") @RequestParam String paymentKey,
		@Parameter(description = "결제 금액") @RequestParam long amount) {
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
	@Operation(summary = "주문 완료 페이지", description = "주문 완료 페이지 제공")
	@GetMapping("/order/confirm")
	public String getConfirmOrder(@Parameter(description = "주문 상품 정보")
		                          @CookieValue(name = "orderCart") String encodedCart,
								  @Parameter(hidden = true) HttpServletRequest request,
		                          @Parameter(hidden = true) HttpServletResponse response) throws JsonProcessingException {
		// 완료된 선택된 장바구니 데이터 지운 후
		String orderCartJson = new String(Base64.getDecoder().decode(encodedCart), StandardCharsets.UTF_8);
		RequestCartOrderDTO orderRequest = objectMapper.readValue(orderCartJson, RequestCartOrderDTO.class);

		String memberId = "";
		String guestKey = "";
		if (JwtHasToken.hasToken(request)) {
			memberId = JwtGetMemberId.jwtGetMemberId(request);
		} else {
			guestKey = CookieUtil.getCookieValue("guestKey", request);
			if (Objects.isNull(guestKey)) {
				guestKey = UUID.randomUUID().toString();
				CookieUtil.setCookie("guestKey", response, guestKey);
			}
		}

		Integer cartItemsCounts = cartService.deleteOrderCompleteCartItems(new RequestDeleteCartOrderDTO(memberId, guestKey, orderRequest.getProductIds(), orderRequest.getCartQuantities()));
		request.getSession().setAttribute(CART_ITEMS_COUNTS, cartItemsCounts);

		// 쿠키 삭제
		CookieUtil.clearCookie("orderCart", response);

		return "payment/confirmation";
	}

	/**
	 * 주문 실패 시 이동할 페이지
	 */
	@Operation(summary = "주문 실패 페이지", description = "주문 실패 페이지 제공")
	@GetMapping("/order/fail")
	public String getFailOrder() {
		return "payment/fail";
	}

	/**
	 * 결제 모달을 끌 시 호출 할 주문서 삭제 요청
	 */
	@Operation(summary = "외부 결제 모달 끌 시 주문서 삭제", description = "외부 결제 모달 끌 시 DB에 저장된 주문서 제거")
	@PostMapping("/order/cancel")
	public ResponseEntity<Void> deleteOrder(@Parameter(description = "주문 코드") @RequestParam String orderId) {
		return orderService.deleteOrder(orderId);
	}

	@Operation(summary = "회원 주문 내역 조회", description = "회원의 주문 내역을 필터링 하여 확인 가능한 페이지 제공")
	@JwtTokenCheck
	@GetMapping("/mypage/orders")
	public String getMemberOrders(Model model, HttpServletRequest request,
		@PageableDefault(page = 0, size = 10) Pageable pageable,
		@Parameter(description = "주문 상태") @RequestParam(required = false) String status,
		@Parameter(description = "주문 일자 검색 시작 일", example = "2025-01-01") @RequestParam(required = false) String startDate,
		@Parameter(description = "주문 일자 검색 끝 일", example = "2025-12-01") @RequestParam(required = false) String endDate,
		@Parameter(description = "주문 코드") @RequestParam(required = false) String orderCode) {

		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		ResponseEntity<PageResponse<ResponseOrderDTO>> response =
			orderService.getOrdersByMemberId(pageable, memberId, status, startDate, endDate, orderCode);
		PageResponse<ResponseOrderDTO> pageResponse = response.getBody();
		Page<ResponseOrderDTO> orders = PageResponseConverter.toPage(pageResponse);
		model.addAttribute("orders", orders);
		return "member/mypage/orders";
	}

	/**
	 * 회원이 자신의 주문 특정 내역 조회
	 */
	@Operation(summary = "회원 주문 상세 조회", description = "회원의 특정 주문 상세 정보를 확인하는 페이지 제공")
	@JwtTokenCheck
	@GetMapping("/mypage/orders/{order-code}")
	public String getMemberOrderDetails(Model model, @Parameter(description = "주문 코드") @PathVariable(name = "order-code") String orderCode) {
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
		LocalDate shipmentDate = order.getShipmentDate();
		LocalDate now = LocalDate.now();

		boolean isReturnAvailable = false;
		boolean isChangeOfMindReturnAvailable = false;
		if (shipmentDate != null) {
			long daysBetween = ChronoUnit.DAYS.between(shipmentDate, now);
			isReturnAvailable = daysBetween <= 30;
			isChangeOfMindReturnAvailable = daysBetween <= 10;
		}

		Boolean isReviewed = reviewService.isReviewedByOrder(orderCode);

		model.addAttribute("isReturnAvailable", isReturnAvailable);
		model.addAttribute("isChangeOfMindReturnAvailable", isChangeOfMindReturnAvailable);
		model.addAttribute("order", order);
		model.addAttribute("orderDetails", orderDetails);
		model.addAttribute("productAmount", productAmount);
		model.addAttribute("isReviewed", isReviewed);

		return "member/mypage/order-details";
	}

	/**
	 * 회원이 배송 시작 전인 특정 주문에 대해 주문을 취소하는 기능
	 */
	@Operation(summary = "주문 취소", description = "회원이 배송 전인 상품에 대하여 주문 취소")
	@JwtTokenCheck
	@DeleteMapping("/mypage/orders/{order-code}")
	public ResponseEntity<Void> cancelOrder(
		@Parameter(description = "주문 코드") @PathVariable(name = "order-code") String orderCode) {
		return orderService.cancelOrder(orderCode);
	}

	/**
	 * 회원의 반품 요청
	 */
	@Operation(summary = "반품 처리", description = "회원이 단순 변심, 파손에 의한 상품에 대하여 반품 요청 처리")
	@PostMapping("/order/return")
	public ResponseEntity<Void> returnOrder(
		@Parameter(description = "반품 요청 정보") @RequestBody @Validated RequestOrderReturnDTO returnDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		return orderService.returnOrder(returnDTO);
	}

	@Operation(summary = "반품 내역 페이지", description = "회원이 반품한 주문에 대한 내역 페이지 제공")
	@GetMapping("/mypage/return")
	public String getReturnOrders(Model model, HttpServletRequest request, Pageable pageable) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		ResponseEntity<PageResponse<ResponseOrderReturnDTO>> response =
			orderService.getReturnOrdersByMemberId(pageable, memberId);
		Page<ResponseOrderReturnDTO> returns = PageResponseConverter.toPage(response.getBody());
		model.addAttribute("returns", returns);

		return "member/mypage/order-returns";
	}

	@Operation(summary = "반품 상세 내역 페이지", description = "회원이 반품 상세 정보에 대해 확인 가능한 페이지 제공")
	@GetMapping("/mypage/return/{order-code}")
	public String getReturnOrderDetails(Model model,
		@Parameter(description = "주문 코드") @PathVariable(name = "order-code") String orderCode) {

		ResponseOrderReturnDTO returnDTO = orderService.getReturnOrderByOrderCode(orderCode).getBody();
		model.addAttribute("returnDTO", returnDTO);
		return "member/mypage/order-return-detail";
	}

	@Operation(summary = "비회원 주문 내역 확인 페이지", description = "비회원이 이메일, 비밀번호로 주문했던 내역을 확인가능한 페이지 제공")
	@GetMapping("/customers/{customer-id}/orders")
	public String getCustomerOrders(Model model,
		@Parameter(description = "비회원 식별 번호") @PathVariable(name = "customer-id") long customerId,
		@PageableDefault(page = 0, size = 10) Pageable pageable) {
		ResponseEntity<PageResponse<ResponseOrderDTO>> response = orderService.getOrdersByCustomerId(pageable, customerId);
		PageResponse<ResponseOrderDTO> pageResponse = response.getBody();
		Page<ResponseOrderDTO> orders = PageResponseConverter.toPage(pageResponse);
		model.addAttribute("orders", orders);
		model.addAttribute("customerId", customerId);
		return "customer/orders";
	}


	@Operation(summary = "비회원 주문 상세 페이지", description = "비회원이 특정 주문에 대해 확인 가능한 페이지 제공")
	@GetMapping("/customers/{customer-id}/orders/{order-code}")
	public String getCustomerOrderDetails(Model model,
		@Parameter(description = "비회원 식별 번호") @PathVariable(name = "customer-id") long customerId,
		@Parameter(description = "주문 코드") @PathVariable(name = "order-code") String orderCode) {
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
		LocalDate shipmentDate = order.getShipmentDate();
		LocalDate now = LocalDate.now();

		boolean isReturnAvailable = false;
		boolean isChangeOfMindReturnAvailable = false;
		if (shipmentDate != null) {
			long daysBetween = ChronoUnit.DAYS.between(shipmentDate, now);
			isReturnAvailable = daysBetween <= 30;
			isChangeOfMindReturnAvailable = daysBetween <= 10;
		}

		model.addAttribute("isReturnAvailable", isReturnAvailable);
		model.addAttribute("isChangeOfMindReturnAvailable", isChangeOfMindReturnAvailable);
		model.addAttribute("order", order);
		model.addAttribute("orderDetails", orderDetails);
		model.addAttribute("productAmount", productAmount);

		return "customer/order-details";
	}
}
