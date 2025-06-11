package com.nhnacademy.front.account.memberrank.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.member.exception.NotFoundMemberRankNameException;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberIdDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.account.memberrank.model.dto.response.ResponseMemberRankDTO;
import com.nhnacademy.front.account.memberrank.service.MemberRankService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.EmptyResponseException;
import com.nhnacademy.front.home.model.dto.response.ResponseHomeMemberNameDTO;
import com.nhnacademy.front.home.service.HomeService;
import com.nhnacademy.front.jwt.parser.JwtGetMemberId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "마이페이지 회원 등급", description = "마이페이지 회원 등급 화면 및 기능 제공")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/rank")
public class MemberRankController {

	private final MemberService memberService;
	private final MemberMypageService memberMypageService;
	private final MemberRankService memberRankService;
	private final HomeService homeService;

	@Operation(summary = "마이페이지 회원 등급 페이지", description = "마이페이지 회원 등급 화면 및 데이터 요청",
		responses = {
			@ApiResponse(responseCode = "201", description = "마이페이지 회원 등급 요청 및 성공 응답"),
			@ApiResponse(responseCode = "500", description = "마이페이지 회원 이름 요청 및 실패 응답",
				content = @Content(schema = @Schema(implementation = EmptyResponseException.class))),
			@ApiResponse(responseCode = "500", description = "마이페이지 회원 등급 요청 및 실패 응답",
				content = @Content(schema = @Schema(implementation = NotFoundMemberRankNameException.class)))
		})
	@JwtTokenCheck
	@GetMapping
	public String getMemberRank(HttpServletRequest request, Model model) {
		ResponseHomeMemberNameDTO responseHomeMemberNameDTO = homeService.getMemberNameFromHome(request);

		model.addAttribute("memberName", responseHomeMemberNameDTO.getMemberName());
		model.addAttribute("memberRole", responseHomeMemberNameDTO.getMemberRole());

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
