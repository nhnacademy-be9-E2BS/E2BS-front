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
import com.nhnacademy.front.common.exception.ValidationFailedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberEmailDormantController {

	private final MemberDormantService memberDormantService;

	/**
	 * 인증번호 화면 Controller
	 */
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
	@PostMapping("/dormant/email")
	public String postEmailAuthentication(
		@Validated @ModelAttribute RequestDormantEmailNumberDTO requestDormantEmailNumberDTO,
		BindingResult bindingResult,
		HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			throw new ValidationFailedException(bindingResult);
		}

		if (request.getSession().getAttribute("dormantMemberId") == null) {
			throw new DormantProcessingException("휴면 해제 과정에서 오류가 발생했습니다. 다시 시도해 주세요.");
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
