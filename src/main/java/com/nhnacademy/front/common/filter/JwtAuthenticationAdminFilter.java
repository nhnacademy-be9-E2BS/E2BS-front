package com.nhnacademy.front.common.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nhnacademy.front.account.auth.model.dto.request.RequestJwtTokenDTO;
import com.nhnacademy.front.account.auth.service.AuthService;
import com.nhnacademy.front.account.member.service.MemberService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class JwtAuthenticationAdminFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final AuthService authService;
	private final MemberService memberService;

	public JwtAuthenticationAdminFilter(AuthenticationManager authenticationManager,
		AuthService authService,
		MemberService memberService) {
		this.authenticationManager = authenticationManager;
		this.authService = authService;
		this.memberService = memberService;
		setFilterProcessesUrl("/admin/admin/login");
	}

	/**
	 *	클라이언트 로그인 form 아이디와 비밀번호를 입력한 값을 가져와서 UsernamePasswordAuthenticationToken 인증 토큰을 생성
	 *	AuthenticationManager 에게 인증을 위임하는 메서드
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException {
		HttpSession session = request.getSession();
		if (Objects.nonNull(session) && Objects.isNull(session.getAttribute("memberState"))) {
			session.removeAttribute("memberState");
		}

		String memberId = request.getParameter("memberId");
		String password = request.getParameter("customerPassword");

		UsernamePasswordAuthenticationToken authRequest =
			new UsernamePasswordAuthenticationToken(memberId, password);

		return authenticationManager.authenticate(authRequest);
	}

	/**
	 *  인증을 성공했을 때 콜백 메서드
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain,
		Authentication authentication) throws IOException, ServletException {

		boolean isAdmin = authentication.getAuthorities().stream()
			.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
		if (!isAdmin) {
			SecurityContextHolder.clearContext();
			String errorMessage = URLEncoder.encode("관리자 아이디로 로그인 해주세요.", StandardCharsets.UTF_8);
			response.sendRedirect("/admin/login?error=" + errorMessage);
			return;
		}

		String memberId = authentication.getName();
		String memberState = memberService.getMemberState(memberId);
		if (memberState.equals("DORMANT")) {
			SecurityContextHolder.clearContext();
			request.getSession().setAttribute("dormantMemberId", memberId);
			request.getSession().setAttribute("dormantCnt", 0);
			request.getSession().setAttribute("memberState", memberState);
			response.sendRedirect("/admin/login");
			return;
		}

		RequestJwtTokenDTO tokenDTO = new RequestJwtTokenDTO(memberId);
		authService.postAuthCreateJwtToken(tokenDTO, response, request);
		chain.doFilter(request, response);
	}

}
