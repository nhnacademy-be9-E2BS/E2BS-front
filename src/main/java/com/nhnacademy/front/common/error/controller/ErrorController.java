package com.nhnacademy.front.common.error.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ErrorController {

	private final ErrorMessageLoader errorMessageLoader;

	@GetMapping("/error-msg")
	public ResponseEntity<String> getErrorMessage(@RequestParam("code") String code) {
		String message = errorMessageLoader.getMessage(code);

		return ResponseEntity.ok(message);
	}

	@GetMapping("/error/404")
	public String getError404(@RequestParam(value = "errorMessage") String errorMessage, Model model) {
		if (Objects.isNull(errorMessage)) {
			model.addAttribute("errorMessage", "페이지를 찾을 수 없습니다.");
		} else {
			model.addAttribute("errorMessage", errorMessage);
		}

		return "error/404";
	}

	@GetMapping("/error/500")
	public String getError500(@RequestParam(value = "errorMessage") String errorMessage, Model model) {
		if (Objects.isNull(errorMessage)) {
			model.addAttribute("errorMessage", "서버 오류가 발생했습니다.");
		} else {
			model.addAttribute("errorMessage", errorMessage);
		}

		return "error/500";
	}

}
