package com.nhnacademy.front.account.memberrank.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.account.memberrank.model.dto.response.ResponseMemberRankDTO;
import com.nhnacademy.front.account.memberrank.service.MemberRankService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/rank")
public class MemberRankController {

	private final MemberService memberService;
	private final MemberMypageService memberMypageService;
	private final MemberRankService memberRankService;

	@GetMapping
	public String getMemberRank(HttpServletRequest request, Model model) {
		String memberId = JwtGetMemberId.jwtGetMemberId(request);

		RequestMemberIdDTO requestMemberIdDTO = new RequestMemberIdDTO(memberId);
		String memberName = memberService.getMemberName(request);
		String rankName = String.valueOf(memberMypageService.getMemberRankName(request));
		List<ResponseMemberRankDTO> memberRanks = memberRankService.getMemberRank(requestMemberIdDTO.getMemberId());

		model.addAttribute("memberName", memberName);
		model.addAttribute("rankName", rankName);
		model.addAttribute("memberRanks", memberRanks);

		log.info("memberRankTierBonusRate:{}", memberRanks.get(0).getMemberRankTierBonusRate());
		log.info("memberRankTierBonusRate:{}", memberRanks.get(1).getMemberRankTierBonusRate());

		return "member/mypage/memberrank/mypage-rank";
	}

}
