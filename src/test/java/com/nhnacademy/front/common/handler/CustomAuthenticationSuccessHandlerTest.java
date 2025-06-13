package com.nhnacademy.front.common.handler;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.cart.service.CartService;
import com.nhnacademy.front.common.util.CookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationSuccessHandlerTest {

	@Mock
	private CartService cartService;

	@Mock
	private MemberService memberService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private Authentication authentication;

	@Mock
	private HttpSession session;

	@InjectMocks
	private CustomAuthenticationSuccessHandler handler;

	@BeforeEach
	void setUp() {
		handler = new CustomAuthenticationSuccessHandler(cartService, memberService);
	}

	@Test
	@DisplayName("로그인 성공 테스트 - 게스트 키 있음 + orderCart 쿠키 있음")
	void onAuthenticationSuccessWithGuestKeyAndOrderCart() throws Exception {
		// given
		String memberId = "member123";
		when(authentication.getName()).thenReturn(memberId);
		when(memberService.getMemberState(memberId)).thenReturn("MEMBER");
		when(request.getSession()).thenReturn(session);
		when(cartService.mergeCartItemsToMemberFromGuest(any())).thenReturn(5);
		when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("orderCart", "Y")});

		try (MockedStatic<CookieUtil> mocked = Mockito.mockStatic(CookieUtil.class)) {
			mocked.when(() -> CookieUtil.getCookieValue("guestKey", request)).thenReturn("guest123");
			mocked.when(() -> CookieUtil.clearCookie("guest123", response)).thenCallRealMethod();

			CustomAuthenticationSuccessHandler spyHandler = Mockito.spy(handler);

			// when
			spyHandler.onAuthenticationSuccess(request, response, authentication);

			// then
			verify(session).setAttribute("cartItemsCounts", 5);
			verify(spyHandler).setDefaultTargetUrl("/members/order");
		}
	}

	@Test
	@DisplayName("로그인 성공 테스트 - 게스트 키 없음 + orderCart 없음")
	void onAuthenticationSuccessWithoutGuestKey() throws Exception {
		// given
		String memberId = "member123";
		when(authentication.getName()).thenReturn(memberId);
		when(memberService.getMemberState(memberId)).thenReturn("MEMBER");
		when(request.getSession()).thenReturn(session);
		when(cartService.getCartItemsCountsForMember(memberId)).thenReturn(3);
		when(request.getCookies()).thenReturn(null);

		try (MockedStatic<CookieUtil> mocked = Mockito.mockStatic(CookieUtil.class)) {
			mocked.when(() -> CookieUtil.getCookieValue("guestKey", request)).thenReturn(null);

			CustomAuthenticationSuccessHandler spyHandler = Mockito.spy(handler);

			// when
			spyHandler.onAuthenticationSuccess(request, response, authentication);

			// then
			verify(session).setAttribute("cartItemsCounts", 3);
			verify(spyHandler).setDefaultTargetUrl("/");
		}
	}

	@Test
	@DisplayName("로그인 성공 테스트 - 회원이 아닌 경우 (ADMIN 등)")
	void onAuthenticationSuccessWithNonMemberRole() throws Exception {
		// given
		String memberId = "admin123";
		when(authentication.getName()).thenReturn(memberId);
		when(memberService.getMemberState(memberId)).thenReturn("ADMIN");

		CustomAuthenticationSuccessHandler spyHandler = Mockito.spy(handler);

		// when
		spyHandler.onAuthenticationSuccess(request, response, authentication);

		// then
		verify(spyHandler).setDefaultTargetUrl("/");
		verify(spyHandler).onAuthenticationSuccess(request, response, authentication);
	}

}
