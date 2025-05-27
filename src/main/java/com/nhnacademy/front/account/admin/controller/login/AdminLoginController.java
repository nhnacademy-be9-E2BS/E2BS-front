package com.nhnacademy.front.account.admin.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/login")
public class AdminLoginController {

	@GetMapping
	public String getAdminLogin() {
		return "admin/login/login";
	}

}
