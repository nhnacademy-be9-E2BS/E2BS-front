package com.nhnacademy.front.account.member.controller.mypage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MemberMypageController {

	@GetMapping
	public String getMypage() {
		return "member/mypage/mypage";
	}

}
