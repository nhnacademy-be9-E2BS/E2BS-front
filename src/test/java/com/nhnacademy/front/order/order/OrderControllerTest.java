package com.nhnacademy.front.order.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.account.address.service.AddressService;
import com.nhnacademy.front.account.customer.model.domain.Customer;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.cart.model.dto.request.RequestCartOrderDTO;
import com.nhnacademy.front.cart.service.CartService;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.common.interceptor.CategoryInterceptor;
import com.nhnacademy.front.common.interceptor.MemberNameAndRoleInterceptor;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.coupon.membercoupon.service.MemberCouponService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;
import com.nhnacademy.front.order.deliveryfee.model.dto.response.ResponseDeliveryFeeDTO;
import com.nhnacademy.front.order.deliveryfee.service.DeliveryFeeSevice;
import com.nhnacademy.front.order.order.controller.OrderController;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderDTO;
import com.nhnacademy.front.order.order.model.dto.request.RequestOrderDetailDTO;
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
import com.nhnacademy.front.product.category.service.UserCategoryService;
import com.nhnacademy.front.product.product.service.ProductService;
import com.nhnacademy.front.review.service.ReviewService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@WithMockUser(username = "admin", roles = {"ADMIN", "MEMBER", "USER"})
@WebMvcTest(controllers = OrderController.class)
@ActiveProfiles("dev")
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OrderService orderService;

	@MockitoBean
	private DeliveryFeeSevice deliveryFeeSevice;

	@MockitoBean
	private ProductService productService;

	@MockitoBean
	private WrapperService wrapperService;

	@MockitoBean
	private MemberMypageService memberMypageService;

	@MockitoBean
	private AddressService addressService;

	@MockitoBean
	private MemberCouponService memberCouponService;

	@MockitoBean
	private CartService cartService;

	@MockitoBean
	private UserCategoryService userCategoryService;

	@MockitoBean
	private ReviewService reviewService;

	@MockitoBean
	private CategoryInterceptor categoryInterceptor;

	@MockitoBean
	private MemberNameAndRoleInterceptor memberNameAndRoleInterceptor;

	@MockitoBean
	private ErrorMessageLoader errorMessageLoader;

	@BeforeEach
	void setUp() throws Exception {
		when(categoryInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(memberNameAndRoleInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Autowired
	private ObjectMapper objectMapper;

	private RequestOrderDTO getTestOrderDTO() {
		RequestOrderDTO orderDTO = new RequestOrderDTO();
		orderDTO.setDeliveryFeeId(1L);
		orderDTO.setCustomerId(100L);
		orderDTO.setOrderReceiverName("테스트");
		orderDTO.setOrderReceiverPhone("01012345678");
		orderDTO.setOrderAddressCode("12345");
		orderDTO.setOrderAddressInfo("서울특별시 강남구 도산대로");
		orderDTO.setOrderAddressExtra("도곡빌딩 3층");
		orderDTO.setOrderPointAmount(5000L);
		orderDTO.setOrderPaymentStatus(true);
		orderDTO.setOrderPaymentAmount(25000L);
		return orderDTO;
	}

	private RequestOrderDetailDTO getTestOrderDetailDTO() {
		RequestOrderDetailDTO orderDetailDTO = new RequestOrderDetailDTO();
		orderDetailDTO.setProductId(10L);
		orderDetailDTO.setOrderCode("TEST-ORDER-CODE");
		orderDetailDTO.setWrapperId(123L);
		orderDetailDTO.setOrderQuantity(3);
		orderDetailDTO.setOrderDetailPerPrice(8900L);
		return orderDetailDTO;
	}

	@Test
	@DisplayName("회원 주문서 페이지 접근 확인")
	void testGetCheckOut() throws Exception {
		// given
		RequestCartOrderDTO dto = new RequestCartOrderDTO(List.of(1L, 2L), List.of(2, 3));
		String json = objectMapper.writeValueAsString(dto);
		String encodedCart = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));


		PageResponse<ResponseWrapperDTO> wrappers = mock(PageResponse.class);
		ResponseMemberInfoDTO member = new ResponseMemberInfoDTO();
		member.setCustomer(new Customer(1L, "test@email.com", "1234", "test"));

		when(productService.getProducts(any())).thenReturn(new ArrayList<>());
		when(wrapperService.getWrappersBySaleable(Pageable.unpaged())).thenReturn(wrappers);
		when(wrappers.getContent()).thenReturn(new ArrayList<>());
		when(memberMypageService.getMemberInfo(any())).thenReturn(member);
		when(memberMypageService.getMemberPoint(any())).thenReturn(0L);


		when(deliveryFeeSevice.getCurrentDeliveryFee()).thenReturn(mock(ResponseDeliveryFeeDTO.class));
		mockMvc.perform(get("/members/order")
				.cookie(new Cookie("orderCart", encodedCart)))
			.andExpect(status().isOk())
			.andExpect(view().name("payment/member-checkout"));
	}

	@Test
	@DisplayName("비회원 주문서 페이지 접근 확인")
	void testGetCheckOut_customer() throws Exception {
		PageResponse<ResponseWrapperDTO> wrappers = mock(PageResponse.class);

		when(productService.getProducts(any())).thenReturn(new ArrayList<>());
		when(wrapperService.getWrappersBySaleable(Pageable.unpaged())).thenReturn(wrappers);
		when(wrappers.getContent()).thenReturn(new ArrayList<>());

		String orderCart = "{\"productIds\":[1,2],\"cartQuantities\":[1,2]}";
		String encodedCart = Base64.getEncoder().encodeToString(orderCart.getBytes(StandardCharsets.UTF_8));


		when(deliveryFeeSevice.getCurrentDeliveryFee()).thenReturn(mock(ResponseDeliveryFeeDTO.class));
		mockMvc.perform(post("/customers/order")
				.with(csrf())
				.cookie(new Cookie("orderCart", encodedCart)) // 쿠키 추가
				.param("customerId", "1")
				.param("customerName", "customer")
				.param("requestCartOrder.productIds[0]", "1")
				.param("requestCartOrder.productIds[1]", "2")
				.param("requestCartOrder.cartQuantities[0]", "1")
				.param("requestCartOrder.cartQuantities[1]", "2"))
			.andExpect(status().isOk())
			.andExpect(view().name("payment/customer-checkout"));
	}

	@Test
	@DisplayName("토스 결제 주문 시 주문서 저장")
	void testPostCheckOut() throws Exception {
		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO(getTestOrderDTO(),
			new ArrayList<RequestOrderDetailDTO>(Arrays.asList(getTestOrderDetailDTO())));

		ResponseOrderResultDTO responseDTO = new ResponseOrderResultDTO();

		when(orderService.createOrder(any(RequestOrderWrapperDTO.class)))
			.thenReturn(ResponseEntity.ok(responseDTO));

		mockMvc.perform(post("/order/tossPay")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("토스 결제 주문 시 주문서 저장 - 잘못된 요청")
	void testPostCheckOutFail() throws Exception {
		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO();

		mockMvc.perform(post("/order/tossPay")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	@Test
	@DisplayName("포인트 결제 주문 생성 - 성공")
	void testPostPointCheckOut() throws Exception {
		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO(getTestOrderDTO(),
			new ArrayList<RequestOrderDetailDTO>(Arrays.asList(getTestOrderDetailDTO())));

		ResponseOrderResultDTO responseDTO = new ResponseOrderResultDTO();

		when(orderService.createOrder(any(RequestOrderWrapperDTO.class)))
			.thenReturn(ResponseEntity.ok(responseDTO));

		mockMvc.perform(post("/order/point")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("포인트 결제 주문 생성 - 잘못된 요청")
	void testPostPointCheckOutFail() throws Exception {
		RequestOrderWrapperDTO request = new RequestOrderWrapperDTO();

		mockMvc.perform(post("/order/point")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertThat(result.getResolvedException())
				.isInstanceOf(ValidationFailedException.class));
	}

	// @Test
	// @DisplayName("결제 완료 후 결제 승인 요청 - 성공 응답 시 리다이렉트")
	// void testGetSuccessOrder_success() throws Exception {
	// 	when(orderService.confirmOrder(anyString(), anyString(), anyLong()))
	// 		.thenReturn(ResponseEntity.ok().build());
	//
	// 	mockMvc.perform(get("/order/success")
	// 			.param("orderId", "TEST-ORDER-CODE")
	// 			.param("paymentKey", "TEST-PAYMENT-KEY")
	// 			.param("amount", "10000"))
	// 		.andExpect(status().is3xxRedirection())
	// 		.andExpect(redirectedUrl("/order/confirm"));
	// }

	// @Test
	// @DisplayName("결제 완료 후 결제 승인 요청 - 실패 응답 시 리다이렉트")
	// void testGetSuccessOrder_fail() throws Exception {
	// 	when(orderService.confirmOrder(anyString(), anyString(), anyLong()))
	// 		.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	//
	// 	mockMvc.perform(get("/order/success")
	// 			.param("orderId", "TEST-ORDER-CODE")
	// 			.param("paymentKey", "TEST-PAYMENT-KEY")
	// 			.param("amount", "10000"))
	// 		.andExpect(status().is3xxRedirection())
	// 		.andExpect(redirectedUrl("/order/fail"));
	// }

	@Test
	@DisplayName("결제 완료 페이지 접근")
	void testGetConfirmOrder() throws Exception {
		String orderCart = "{\"productIds\":[1,2],\"cartQuantities\":[1,2]}";
		String encodedCart = Base64.getEncoder().encodeToString(orderCart.getBytes(StandardCharsets.UTF_8));


		mockMvc.perform(get("/order/confirm")
				.cookie(new Cookie("orderCart", encodedCart)))
			.andExpect(status().isOk())
			.andExpect(view().name("payment/confirmation"));
	}

	@Test
	@DisplayName("결제 실패 페이지 접근")
	void testGetFailOrder() throws Exception {
		mockMvc.perform(get("/order/fail"))
			.andExpect(status().isOk())
			.andExpect(view().name("payment/fail"));
	}

	@Test
	@DisplayName("토스 결제 모달 끌 시 주문서 삭제 요청")
	void testDeleteOrder() throws Exception {
		when(orderService.deleteOrder(anyString()))
			.thenReturn(ResponseEntity.ok().build());

		mockMvc.perform(post("/order/cancel")
				.param("orderId", "TEST-ORDER-CODE")
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("getMemberOrders - 회원 주문 내역 조회")
	void testGetMemberOrders() throws Exception {
		// given
		PageResponse<ResponseOrderDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(new ResponseOrderDTO()));
		pageResponse.setNumber(0);
		pageResponse.setSize(10);
		pageResponse.setTotalElements(1L);
		ResponseEntity<PageResponse<ResponseOrderDTO>> responseEntity = ResponseEntity.ok(pageResponse);
		when(orderService.getOrdersByMemberId(any(Pageable.class), any(), any(), any(), any(), any())).thenReturn(responseEntity);

		try (MockedStatic<JwtGetMemberId> mockStatic = mockStatic(JwtGetMemberId.class)) {
			mockStatic.when(() -> JwtGetMemberId.jwtGetMemberId(any(HttpServletRequest.class)))
				.thenReturn("TEST-MEMBERID");
			// when & then
			mockMvc.perform(get("/mypage/orders")
					.param("page", "0")
					.param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(view().name("member/mypage/orders"))
				.andExpect(model().attributeExists("orders"));
		}
	}

	@Test
	@DisplayName("getCustomerOrders - 비회원 주문 내역 조회")
	void testGetCustomerOrders() throws Exception {
		// given
		PageResponse<ResponseOrderDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(new ResponseOrderDTO()));
		pageResponse.setNumber(0);
		pageResponse.setSize(10);
		pageResponse.setTotalElements(1L);
		ResponseEntity<PageResponse<ResponseOrderDTO>> responseEntity = ResponseEntity.ok(pageResponse);
		when(orderService.getOrdersByCustomerId(any(Pageable.class), anyLong())).thenReturn(responseEntity);

		// when & then
		mockMvc.perform(get("/customers/1/orders")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(view().name("customer/orders"))
			.andExpect(model().attributeExists("orders"));

	}

	@Test
	@DisplayName("getMemberOrderDetails - 회원 주문 상세 내역 페이지")
	void testGetMemberOrderDetails() throws Exception {
		// given
		String orderCode = "TEST-ORDER-CODE";

		ResponseOrderDTO order = new ResponseOrderDTO();
		order.setState("WAIT");
		ResponseOrderDetailDTO detail1 = new ResponseOrderDetailDTO();
		detail1.setOrderDetailPerPrice(1000);
		detail1.setOrderQuantity(2);
		detail1.setWrapperPrice(500L);

		ResponseOrderDetailDTO detail2 = new ResponseOrderDetailDTO();
		detail2.setOrderDetailPerPrice(2000);
		detail2.setOrderQuantity(1);

		List<ResponseOrderDetailDTO> orderDetails = List.of(detail1, detail2);

		ResponseOrderWrapperDTO wrapperDTO = new ResponseOrderWrapperDTO();
		wrapperDTO.setOrder(order);
		wrapperDTO.setOrderDetails(orderDetails);

		when(orderService.getOrderByOrderCode(orderCode)).thenReturn(ResponseEntity.ok(wrapperDTO));

		// when & then
		mockMvc.perform(get("/mypage/orders/" + orderCode))
			.andExpect(status().isOk())
			.andExpect(view().name("member/mypage/order-details"))
			.andExpect(model().attributeExists("order"))
			.andExpect(model().attributeExists("orderDetails"))
			.andExpect(model().attributeExists("productAmount"));
	}

	@Test
	@DisplayName("getCustomerOrderDetails - 비회원 주문 상세 내역 페이지")
	void testGetCustomerOrderDetails() throws Exception {
		// given
		String orderCode = "TEST-ORDER-CODE";

		ResponseOrderDTO order = new ResponseOrderDTO();
		order.setState("WAIT");
		ResponseOrderDetailDTO detail1 = new ResponseOrderDetailDTO();
		detail1.setOrderDetailPerPrice(1000);
		detail1.setOrderQuantity(2);
		detail1.setWrapperPrice(500L);

		ResponseOrderDetailDTO detail2 = new ResponseOrderDetailDTO();
		detail2.setOrderDetailPerPrice(2000);
		detail2.setOrderQuantity(1);

		List<ResponseOrderDetailDTO> orderDetails = List.of(detail1, detail2);

		ResponseOrderWrapperDTO wrapperDTO = new ResponseOrderWrapperDTO();
		wrapperDTO.setOrder(order);
		wrapperDTO.setOrderDetails(orderDetails);

		when(orderService.getOrderByOrderCode(orderCode)).thenReturn(ResponseEntity.ok(wrapperDTO));

		// when & then
		mockMvc.perform(get("/customers/1/orders/" + orderCode))
			.andExpect(status().isOk())
			.andExpect(view().name("customer/order-details"))
			.andExpect(model().attributeExists("order"))
			.andExpect(model().attributeExists("orderDetails"))
			.andExpect(model().attributeExists("productAmount"));
	}

	@Test
	@DisplayName("cancelOrder-주문 취소")
	void testCancelOrder() throws Exception {
		String orderCode = "TEST-ORDER-CODE";
		when(orderService.cancelOrder(anyString())).thenReturn(ResponseEntity.ok().build());
		mockMvc.perform(delete("/mypage/orders/" + orderCode)
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("returnOrder-반품 처리")
	void testReturnOrder() throws Exception {
		RequestOrderReturnDTO returnDTO = new RequestOrderReturnDTO("TEST-ORDER-CODE", "BREAK", "BREAK");
		when(orderService.cancelOrder(anyString())).thenReturn(ResponseEntity.ok().build());
		mockMvc.perform(post("/order/return")
				.content(objectMapper.writeValueAsString(returnDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("getReturnOrders-반품 내역 조회")
	void testGetReturnOrders() throws Exception {
		PageResponse<ResponseOrderReturnDTO> pageResponse = new PageResponse<>();
		pageResponse.setContent(List.of(new ResponseOrderReturnDTO()));
		pageResponse.setNumber(0);
		pageResponse.setSize(10);
		pageResponse.setTotalElements(1L);
		ResponseEntity<PageResponse<ResponseOrderReturnDTO>> responseEntity = ResponseEntity.ok(pageResponse);
		when(orderService.getReturnOrdersByMemberId(any(Pageable.class), any())).thenReturn(responseEntity);

		mockMvc.perform(get("/mypage/return")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("getReturnOrderDetails-반품 상세 정보 조회")
	void testGetReturnOrderDetails() throws Exception {
		String orderCode = "TEST-ORDER-CODE";
		ResponseEntity<ResponseOrderReturnDTO> responseEntity = ResponseEntity.ok(new ResponseOrderReturnDTO());
		when(orderService.getReturnOrderByOrderCode(orderCode)).thenReturn(responseEntity);

		mockMvc.perform(get("/mypage/return/" + orderCode))
			.andExpect(status().isOk());
	}
}
