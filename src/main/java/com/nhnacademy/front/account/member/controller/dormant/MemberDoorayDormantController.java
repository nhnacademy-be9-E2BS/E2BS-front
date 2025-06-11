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
import com.nhnacademy.front.account.member.model.dto.request.RequestDoorayAuthenticationDTO;
import com.nhnacademy.front.account.member.model.dto.request.RequestDormantDoorayNumberDTO;
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

@Tag(name = "회원 휴면 상태 (Dooray)", description = "회원 로그인 시 휴면 상태 처리 기능 제공")
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberDoorayDormantController {

	private final MemberDormantService memberDormantService;

	/**
	 * Dooray, Email 선택하는 화면 Controller
	 */
	@Operation(summary = "휴면 상태 처리 방법 선택 페이지", description = "휴면 상태 처리 Dooray, Email 방법 선택 화면 제공",
		responses = {
			@ApiResponse(responseCode = "201", description = "휴면 상태 처리 방법 선택 화면 제공"),
			@ApiResponse(responseCode = "400", description = "휴면 상태가 아닌 경우 에러 처리",
				content = @Content(schema = @Schema(implementation = DormantProcessingException.class)))
		})
	@GetMapping("/login/dormant")
	public String getMemberDormant(HttpServletRequest request, Model model) {
		if (request.getSession().getAttribute("dormantMemberId") == null) {
			throw new DormantProcessingException();
		}
		String memberId = request.getSession().getAttribute("dormantMemberId").toString();
		model.addAttribute("memberId", memberId);

		request.getSession().setAttribute("remainingSeconds", 0);

		return "member/dormant/dormant-method";
	}

	/**
	 * 인증번호 화면 Controller
	 */
	@Operation(summary = "휴면 상태 Dooray 방식 페이지", description = "휴면 상태 Dooray 방식 화면 제공")
	@GetMapping("/dormant/dooray/{member-id}")
	public String getMemberDormantDooray(@PathVariable("member-id") String memberId, Model model,
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

		return "member/dormant/dormant-dooray";
	}

	/**
	 * Dooray 인증 번호 전송 Controller
	 */
	@Operation(summary = "휴면 상태 Dooray 인증 번호 전송", description = "휴면 상태 Dooray 인증 번호 제공하는 기능")
	@PostMapping("/dormant/dooray/{member-id}")
	public String postDoorayAuthenticationNumber(@PathVariable("member-id") String memberId,
		HttpServletRequest request) {
		// 6자리 무작위 값을 생성해서 redis 에 저장하는 메서드
		String authenticationNumber = memberDormantService.createDoorayAuthenticationNumber(memberId);
		String text = String.format("[E2BS] 인증번호 [%s] 타인에게 알려주지 마세요.\n3분 이내에 입력해주세요.", authenticationNumber);

		memberDormantService.sendDoorayMessageAuthenticationNumber(
			new RequestDoorayAuthenticationDTO("E2BS 관리자", text)
		);

		HttpSession session = request.getSession();
		session.setAttribute("remainingSeconds", 180);
		session.setAttribute("remainingCnt", 0);

		return "redirect:/member/dormant/dooray/" + memberId;
	}

	/**
	 * 사용자가 입력한 Dooray 인증 번호가 일치하는 지 확인하는 Controller
	 */
	@Operation(summary = "휴면 상태 Dooray 인증 번호가 일치하는 지 검증", description = "휴면 상태 Dooray 인증번호가 일치하는 지 검증 기능",
		responses = {
			@ApiResponse(responseCode = "302", description = "휴면 상태 처리 성공 후 로그인 페이지로 리다이렉션"),
			@ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ValidationFailedException.class))),
			@ApiResponse(responseCode = "400", description = "회원 아이디 값이 없는 경우 에러 처리",
				content = @Content(schema = @Schema(implementation = DormantProcessingException.class))),
			@ApiResponse(responseCode = "400", description = "인증 번호가 일치하지 않는 경우 에러 처리",
				content = @Content(schema = @Schema(implementation = DormantDoorayNotMatchedNumberException.class))),
			@ApiResponse(responseCode = "500", description = "회원 상태 변경 요청 및 실패 응답",
				content = @Content(schema = @Schema(implementation = DormantProcessingException.class)))
		})
	@PostMapping("/dormant/dooray")
	public String postDoorayAuthentication(@Validated
		@Parameter(description = "회원 상태 변경 요청 DTO", required = true, schema = @Schema(implementation = RequestDormantDoorayNumberDTO.class))
		@ModelAttribute RequestDormantDoorayNumberDTO requestDormantDoorayNumberDTO,
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
		if (!memberDormantService.checkDoorayAuthenticationNumber(requestDormantDoorayNumberDTO, memberId)) {
			throw new DormantDoorayNotMatchedNumberException("인증 번호가 일치하지 않습니다.");
		}

		// 회원의 상태를 Active 로 변경하고 session 값 제거
		memberDormantService.changeMemberStateActive(memberId, request);

		HttpSession session = request.getSession();
		session.removeAttribute("remainingSeconds");
		session.removeAttribute("remainingCnt");
		session.removeAttribute("dormantMemberId");
		session.removeAttribute("dormantCnt");
		session.removeAttribute("memberState");

		return "redirect:/members/login";
	}

}
