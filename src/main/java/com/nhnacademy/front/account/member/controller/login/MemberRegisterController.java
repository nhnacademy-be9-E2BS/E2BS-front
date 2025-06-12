package com.nhnacademy.front.account.member.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.member.model.dto.request.RequestRegisterMemberDTO;
import com.nhnacademy.front.account.member.service.MemberService;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "회원가입", description = "회원 가입 페이지 및 기능 제공")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members/register")
public class MemberRegisterController {

	private final MemberService memberService;

	/**
	 *  회원가입 뷰
	 */
	@Operation(summary = "회원가입 폼 페이지", description = "회원 가입 화면 제공")
	@GetMapping
	public String getRegister() {
		return "member/login/register";
	}

	/**
	 * 회원가입 기능 구현
	 */
	@Operation(summary = "회원가입 요청 처리",
		description = "입력 받은 회원 정보를 바탕으로 회원 생성",
		responses = {
			@ApiResponse(responseCode = "302", description = "회원가입 성공 후 로그인 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class)))
		})
	@PostMapping
	public String createRegister(@Validated
		@Parameter(description = "회원가입 요청 DTO", required = true, schema = @Schema(implementation = RequestRegisterMemberDTO.class))
		@ModelAttribute RequestRegisterMemberDTO requestRegisterMemberDTO,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		memberService.createMember(requestRegisterMemberDTO);

		return "redirect:/members/login";
	}

}
