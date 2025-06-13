package com.nhnacademy.front.common.error.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "ERROR", description = "에러 메세지 변환 및 에러 페이지 화면 제공")
@Controller
@RequiredArgsConstructor
public class ErrorController {
	private static final String ERROR_404 = "error/error-404";
	private static final String ERROR_500 = "error/error-500";
	private static final String ERROR_MESSAGE = "errorMessage";

	private final ErrorMessageLoader errorMessageLoader;

	@Operation(summary = "에러 코드로 메세지 조회", description = "에러 코드에 해당하는 메세지를 변환하는 기능 제공")
	@GetMapping("/error-msg")
	public ResponseEntity<String> getErrorMessage(@RequestParam("code") String code) {
		String message = errorMessageLoader.getMessage(code);

		return ResponseEntity.ok(message);
	}

	@Operation(summary = "404 에러 페이지", description = "404 에러 코드에 해당하는 메세지 및 화면 제공")
	@GetMapping("/error/404")
	public String getError404(@RequestParam(value = "errorMessage") String errorMessage, Model model) {
		return renderErrorPage(errorMessage, model);
	}

	@Operation(summary = "500 에러 페이지", description = "500 에러 코드에 해당하는 메세지 및 화면 제공")
	@GetMapping("/error/500")
	public String getError500(@RequestParam(value = "errorMessage") String errorMessage, Model model) {
		model.addAttribute(ERROR_MESSAGE, errorMessage);

		return ERROR_500;
	}

	@Operation(summary = "404 에러 페이지", description = "404 에러 코드에 해당하는 메세지 및 화면 제공")
	@GetMapping("/error")
	public String get404(@RequestParam(value = "errorMessage") String errorMessage, Model model) {
		return renderErrorPage(errorMessage, model);
	}

	private String renderErrorPage(String errorMessage, Model model) {
		model.addAttribute(ERROR_MESSAGE, errorMessage);
		return ERROR_404;
	}

}
