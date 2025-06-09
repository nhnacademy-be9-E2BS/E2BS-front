package com.nhnacademy.front.account.member.controller.mypage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.member.exception.NotFoundMemberInfoException;
import com.nhnacademy.front.account.member.exception.PasswordNotEqualsException;
import com.nhnacademy.front.account.member.model.dto.request.RequestMemberInfoDTO;
import com.nhnacademy.front.account.member.model.dto.response.ResponseMemberInfoDTO;
import com.nhnacademy.front.account.member.service.MemberMypageService;
import com.nhnacademy.front.common.annotation.JwtTokenCheck;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "마이페이지 개인정보 수정", description = "마이페이지 개인정보 수정 화면 및 기능 제공")
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/info")
public class MemberInfoMypageController {

	private final MemberMypageService memberMypageService;

	/**
	 * 회원 정보를 가져와서 보여주는 뷰
	 */
	@Operation(summary = "마이페이지 개인정보 페이지", description = "마이페이지 개인정보 요청 및 화면 제공",
		responses = {
			@ApiResponse(responseCode = "201", description = "마이페이지 개인정보 데이터 요청 및 성공 응답"),
			@ApiResponse(responseCode = "500", description = "마이페이지 개인정보 데이터 요청 및 실패 응답",
				content = @Content(schema = @Schema(implementation = NotFoundMemberInfoException.class)))
		})
	@JwtTokenCheck
	@GetMapping
	public String getMemberInfo(HttpServletRequest request, Model model) {
		ResponseMemberInfoDTO response = memberMypageService.getMemberInfo(request);

		model.addAttribute("member", response);

		return "member/mypage/memberinfo/member-info";
	}

	/**
	 * 회원 정보를 수정하는 컨트롤러
	 */
	@Operation(summary = "마이페이지 개인정보 수정", description = "마이페이지 개인정보 수정하는 기능 제공",
		responses = {
			@ApiResponse(responseCode = "302", description = "마이페이지 개인정보 수정 요청 및 성공 응답 후 마이페이지 개인정보 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않는 경우 에러 처리", content = @Content(schema = @Schema(implementation = PasswordNotEqualsException.class))),
			@ApiResponse(responseCode = "500", description = "마이페이지 개인정보 수정 요청 및 실패 응답",
				content = @Content(schema = @Schema(implementation = NotFoundMemberInfoException.class)))
		})
	@JwtTokenCheck
	@PutMapping
	public String updateMemberInfo(@Validated
		@Parameter(description = "마이페이지 개인정보 요청 DTO", required = true, schema = @Schema(implementation = RequestMemberInfoDTO.class))
		@ModelAttribute RequestMemberInfoDTO requestMemberInfoDTO,
		BindingResult bindingResult,
		HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}
		memberMypageService.updateMemberInfo(request, requestMemberInfoDTO);

		return "redirect:/mypage/info";
	}

	/**
	 * 회원 탈퇴 메서드
	 */
	@Operation(summary = "마이페이지 회원 탈퇴", description = "마이페이지 회원 탈퇴하는 기능 제공",
		responses = {
			@ApiResponse(responseCode = "302", description = "마이페이지 회원 탈퇴 요청 및 성공 응답 후 메인 화면으로 리다이렉션"),
			@ApiResponse(responseCode = "500", description = "마이페이지 회원 탈퇴 요청 및 실패 응답",
				content = @Content(schema = @Schema(implementation = NotFoundMemberInfoException.class)))
		})
	@JwtTokenCheck
	@PostMapping
	public String withdrawMember(HttpServletRequest request, HttpServletResponse response) {
		memberMypageService.withdrawMember(request, response);

		return "redirect:/";
	}

}
