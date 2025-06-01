package com.nhnacademy.front.common.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.service.AuthService;
import com.nhnacademy.front.cart.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * MemberRole 에 따른 로그인 기능 (MEMBER or ADMIN)
 */
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private static final String ROOT_URL = "/";
	private static final String LOGIN_URL = "/login";
	private static final String ADMIN_LOGIN_URL = "/admin/login";

	private final AuthService authService;
	private final CartService cartService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		                                HttpServletResponse response,
		                                Authentication authentication) throws IOException {

		String url = request.getHeader("Referer"); // 브라우저의 URL 주소를 가져오기 위한 Referer 사용
		String memberId = authentication.getName();

		boolean isAdmin = authentication.getAuthorities().stream()
			.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

		boolean isMember = authentication.getAuthorities().stream()
			.anyMatch(authority -> authority.getAuthority().equals("ROLE_MEMBER"));

		if (isAdmin && !url.contains(ADMIN_LOGIN_URL)) {
			SecurityContextHolder.clearContext(); // Security 초기화
			response.sendRedirect(LOGIN_URL);
		} else if (isMember && url.contains(ADMIN_LOGIN_URL)) {
			SecurityContextHolder.clearContext(); // Security 초기화
			response.sendRedirect(ADMIN_LOGIN_URL);
		} else {
			RequestJwtTokenDTO requestJwtTokenDTO = new RequestJwtTokenDTO(memberId);
			authService.postAuthCreateJwtToken(requestJwtTokenDTO, response, request);

			/**
			 * Spring Security 는 보안상의 이유로 로그인 성공 시 기존 세션을 무효화하고 새로운 세션을 생성하는 동작을 한다.
			 * 이는 세션 고정 공격을 방지하기 위한 기본 방어 메커니즘이다.
			 *
			 * ex) 로그인 전: sessionId = A
			 * 로그인 후: sessionId = B ← 새로운 세션으로 생성됨
			 *
			 * 그래서 로그인 전은 쿠키에 저장한 세션아이디를 꺼내서 적용함
			 */
			HttpSession session = request.getSession();

			// 게스트 키가 있으면 장바구니를 꺼내서 병합 후 항목 개수 적용
			// String guestKey = GuestCookieUtil.getGuestKey(request);
			// if (Objects.nonNull(guestKey)) {
			// 	Integer mergedCount = cartService.mergeCartItemsToMemberFromGuest(new RequestMergeCartItemDTO(memberId, guestKey));
			//
			// 	session.setAttribute("cartItemsCounts", mergedCount);
			// 	GuestCookieUtil.clearGuestCookie(response); // 쿠키 삭제
			// } else {
			// 	// 없으면 기존 회원 장바구니 항목 개수 적용
			// 	session.setAttribute("cartItemsCounts", cartService.getCartItemsCountsForMember(memberId));
			// }

			session.setAttribute("isMember", true);

			response.sendRedirect(ROOT_URL);
		}
	}

}
