package com.nhnacademy.front.account.member.controller.mypage;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MemberMypageController {

	private final MemberService memberService;
	private final MemberMypageService memberMypageService;

	@JwtTokenCheck
	@GetMapping
	public String getMypage(HttpServletRequest request,
		Model model) {

		String memberId = JwtGetMemberId.jwtGetMemberId(request);
		if (Objects.isNull(memberId)) {
			throw new BadRequestException("다시 로그인 해주세요.");
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

		model.addAttribute("memberName", memberName);
		model.addAttribute("couponCnt", couponCnt);
		model.addAttribute("pointAmount", pointAmount);

		return "member/mypage/mypage";
	}

}
