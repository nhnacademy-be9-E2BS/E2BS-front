package com.nhnacademy.front.account.admin.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "관리자 로그인", description = "관리자 로그인 페이지 및 기능 제공")
@Controller
@RequestMapping("/admin/login")
public class AdminLoginController {

	@Operation(summary = "관리자 로그인 폼 페이지", description = "관리자 로그인 화면 제공")
	@GetMapping
	public String getAdminLogin() {
		return "admin/login/login";
	}

}
