package com.nhnacademy.front.account.pointhistory.controller;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.account.pointhistory.model.dto.response.ResponsePointHistoryDTO;
import com.nhnacademy.front.account.pointhistory.service.PointHistoryService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PointHistoryController {

	private final PointHistoryService pointHistoryService;

	@JwtTokenCheck
	@GetMapping("/mypage/pointHistory")
	public String getPointHistory(HttpServletRequest request, @PageableDefault() Pageable pageable, Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		if (Objects.isNull(memberId)) {
			throw new JwtException("JWT token is null");
		}

		PageResponse<ResponsePointHistoryDTO> response = pointHistoryService.getPointHistoryByMemberId(memberId, pageable);
		Page<ResponsePointHistoryDTO> pointHistories = PageResponseConverter.toPage(response);

		model.addAttribute("pointHistories", pointHistories);
		return "member/mypage/point-history";
	}
}
