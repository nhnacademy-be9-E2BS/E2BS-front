package com.nhnacademy.front.account.member.controller.dormant;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.member.exception.DormantDoorayNotMatchedNumberException;
import com.nhnacademy.front.account.member.exception.DormantProcessingException;
import com.nhnacademy.front.account.member.model.dto.request.RequestDormantEmailNumberDTO;
import com.nhnacademy.front.account.member.service.MemberDormantService;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원 휴면 상태 (Email)", description = "회원 휴면 상태 페이지 및 기능 제공")
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberEmailDormantController {

	private final MemberDormantService memberDormantService;

	/**
	 * 인증번호 화면 Controller
	 */
	@Operation(summary = "회원 휴면 Email 방식 페이지", description = "회원 휴면 Email 방식 화면 제공")
	@GetMapping("/dormant/email/{memberId}")
	public String getMemberDormantEmail(@PathVariable("memberId") String memberId, Model model,
		HttpServletRequest request) {
		HttpSession session = request.getSession();
		Integer remainingSeconds = 0;

		if (request.getSession().getAttribute("remainingCnt") != null) {
			Integer remainingCnt = (Integer)request.getSession().getAttribute("remainingCnt");
			request.getSession().setAttribute("remainingCnt", ++remainingCnt);

			if (remainingCnt == 1) {
				remainingSeconds = (Integer)session.getAttribute("remainingSeconds");
			}

		}

		model.addAttribute("memberId", memberId);
		model.addAttribute("remainingSeconds", remainingSeconds);

		return "member/dormant/dormant-email";
	}

	/**
	 * Email 인증 번호 전송 Controller
	 */
	@Operation(summary = "회원 휴면 Email 인증 번호 전송", description = "회원 휴면 Email 인증 번호 전송 기능 제공")
	@PostMapping("/dormant/email/{memberId}")
	public String postEmailAuthenticationNumber(@PathVariable("memberId") String memberId, HttpServletRequest request) {
		String authenticationNumber = memberDormantService.createEmailAuthenticationNumber(memberId);
		String customerEmail = memberDormantService.getMemberEmail(memberId);

		memberDormantService.sendEmailAuthenticationNumber(customerEmail, authenticationNumber);

		HttpSession session = request.getSession();
		session.setAttribute("remainingSeconds", 180);
		session.setAttribute("remainingCnt", 0);

		return "redirect:/member/dormant/email/" + memberId;
	}

	/**
	 * 사용자가 입력한 Email 인증 번호가 일치하는 지 확인하는 Controller
	 */
	@Operation(summary = "회원 휴먼 Email 인증 번호가 일치하는 지 검증 및 회원 상태 변경",
		description = "회원 휴먼 Email 인증 번호가 일치하는 지 검증 기능",
		responses = {
			@ApiResponse(responseCode = "302", description = "회원 휴먼 Email 인증 번호 일치하는 지 검증 및 회원 상태 변경 요청 및 성공 응답 후 로그인 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "400", description = "휴면 회원 아이디가 없는 경우 에러 처리", content = @Content(schema = @Schema(implementation = DormantProcessingException.class))),
			@ApiResponse(responseCode = "400", description = "인증 번호가 일치하지 않는 경우 에러 처리", content = @Content(schema = @Schema(implementation = DormantDoorayNotMatchedNumberException.class))),
			@ApiResponse(responseCode = "500", description = "휴면 회원 상태 변경 요청 및 실패 응답", content = @Content(schema = @Schema(implementation = DormantProcessingException.class)))
		})
	@PostMapping("/dormant/email")
	public String postEmailAuthentication(@Validated
		@Parameter(description = "회원 휴먼 Email 인증 번호 요청 DTO", required = true, schema = @Schema(implementation = RequestDormantEmailNumberDTO.class))
		@ModelAttribute RequestDormantEmailNumberDTO requestDormantEmailNumberDTO,
		BindingResult bindingResult,
		HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		if (request.getSession().getAttribute("dormantMemberId") == null) {
			throw new DormantProcessingException();
		}
		String memberId = request.getSession().getAttribute("dormantMemberId").toString();

		// 회원이 입력한 인증 번호가 일치하는 지 확인
		if (!memberDormantService.checkEmailAuthenticationNumber(requestDormantEmailNumberDTO, memberId)) {
			throw new DormantDoorayNotMatchedNumberException("인증 번호가 일치하지 않습니다.");
		}

		memberDormantService.changeMemberStateActive(memberId, request);

		HttpSession session = request.getSession();
		session.removeAttribute("remainingSeconds");
		session.removeAttribute("remainingCnt");
		session.removeAttribute("dormantMemberId");
		session.removeAttribute("dormantCnt");
		session.removeAttribute("memberState");

		return "redirect:/login";
	}

}
