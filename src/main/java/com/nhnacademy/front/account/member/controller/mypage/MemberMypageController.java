package com.nhnacademy.front.account.member.controller.mypage;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.member.exception.NotFoundMemberRankNameException;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberRecentOrderDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.EmptyResponseException;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "마이페이지", description = "마이페이지 화면 및 기능 제공")
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MemberMypageController {

	private final MemberService memberService;
	private final MemberMypageService memberMypageService;

	@Operation(summary = "마이페이지 페이지", description = "마이페이지 화면에 필요한 데이터 요청 및 화면 제공",
		responses = {
			@ApiResponse(responseCode = "201", description = "마이페이지 화면에 필요한 데이터 요청 및 성공 응답"),
			@ApiResponse(responseCode = "500", description = "마이페이지 화면에 필요한 데이터 요청 및 실패 응답",
				content = @Content(schema = @Schema(implementation = EmptyResponseException.class))),
			@ApiResponse(responseCode = "500", description = "마이페이지 회원 등급 요청 및 실패 응답",
				content = @Content(schema = @Schema(implementation = NotFoundMemberRankNameException.class)))
		})
	@JwtTokenCheck
	@GetMapping
	public String getMypage(HttpServletRequest request, Model model) {

		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		int orderCnt = 0;
		int orderCntFromBack = memberMypageService.getMemberOrder(memberId);
		if (orderCntFromBack > 0) {
			orderCnt = orderCntFromBack;
		}

		RequestMemberIdDTO requestMemberIdDTO = new RequestMemberIdDTO(memberId);
		String memberName = memberService.getMemberName(request);

		int couponCnt = 0;
		int couponCntFromBack = memberMypageService.getMemberCoupon(requestMemberIdDTO);
		if (couponCntFromBack > 0) {
			couponCnt = couponCntFromBack;
		}

		long pointAmount = 0;
		long pointAmountFromBack = memberMypageService.getMemberPoint(requestMemberIdDTO);
		if (pointAmountFromBack > 0) {
			pointAmount = pointAmountFromBack;
		}

		String rankName = String.valueOf(memberMypageService.getMemberRankName(request));
		List<ResponseMemberRecentOrderDTO> responseMemberRecentOrderDTO = memberMypageService.getMemberRecentOrders(
			memberId);

		model.addAttribute("memberId", memberId);
		model.addAttribute("memberName", memberName);
		model.addAttribute("orderCnt", orderCnt);
		model.addAttribute("couponCnt", couponCnt);
		model.addAttribute("pointAmount", pointAmount);
		model.addAttribute("rankName", rankName);
		model.addAttribute("recentOrders", responseMemberRecentOrderDTO);

		return "member/mypage/mypage";
	}

}
