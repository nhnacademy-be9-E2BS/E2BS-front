package com.nhnacademy.front.account.member.controller.login;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nhnacademy.front.account.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberRegisterCheckController {

	private final MemberService memberService;

	@GetMapping("/{member-id}/register")
	public ResponseEntity<Map<String, Boolean>> getCheckMemberId(@PathVariable("member-id") String memberId) {
		boolean idDuplicateCheck = memberService.existsMemberByMemberId(memberId);
		Map<String, Boolean> response = Collections.singletonMap("available", idDuplicateCheck);

		return ResponseEntity.ok(response);
	}

}
