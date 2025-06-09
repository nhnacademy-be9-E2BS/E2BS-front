package com.nhnacademy.front.account.pointhistory.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.account.pointhistory.model.dto.response.ResponsePointHistoryDTO;
import com.nhnacademy.front.account.pointhistory.service.PointHistoryService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.page.PageResponse;
import com.nhnacademy.front.common.page.PageResponseConverter;
import com.nhnacademy.front.home.model.dto.response.ResponseHomeMemberNameDTO;
import com.nhnacademy.front.home.service.HomeService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "포인트 내역", description = "마이페이지 포인트 적립/사용 내역 조회 기능 제공")
@Controller
@RequiredArgsConstructor
public class PointHistoryController {

	private final PointHistoryService pointHistoryService;
	private final MemberMypageService memberMypageService;
	private final HomeService homeService;

	/**
	 * 마이페이지 포인트 내역 조회
	 */
	@Operation(
		summary = "마이페이지 포인트 내역 조회",
		description = "로그인한 사용자의 포인트 적립 및 사용 내역 조회",
		responses = {
			@ApiResponse(responseCode = "200", description = "포인트 내역 조회 성공")
		}
	)
	@JwtTokenCheck
	@GetMapping("/mypage/pointHistory")
	public String getPointHistory(HttpServletRequest request, @PageableDefault Pageable pageable, Model model) {
		ResponseHomeMemberNameDTO responseHomeMemberNameDTO = homeService.getMemberNameFromHome(request);
		model.addAttribute("memberName", responseHomeMemberNameDTO.getMemberName());
		model.addAttribute("memberRole", responseHomeMemberNameDTO.getMemberRole());

		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		PageResponse<ResponsePointHistoryDTO> response = pointHistoryService.getPointHistoryByMemberId(memberId, pageable);
		Page<ResponsePointHistoryDTO> pointHistories = PageResponseConverter.toPage(response);

		Long usablePoint = memberMypageService.getMemberPoint(new RequestMemberIdDTO(memberId));

		model.addAttribute("pointHistories", pointHistories);
		model.addAttribute("usablePoint", usablePoint);
		return "member/mypage/point-history";
	}
}
