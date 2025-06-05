package com.nhnacademy.front.common.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.nhnacademy.front.account.address.exception.DeleteAddressFailedException;
import com.nhnacademy.front.account.address.exception.GetAddressFailedException;
import com.nhnacademy.front.account.address.exception.SaveAddressFailedException;
import com.nhnacademy.front.account.address.exception.UpdateAddressFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMemberDeleteFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMemberUpdateFailedException;
import com.nhnacademy.front.account.admin.exception.AdminSettingsMembersFailedException;
import com.nhnacademy.front.account.auth.exception.SaveJwtTokenProcessException;
import com.nhnacademy.front.account.customer.exception.CustomerLoginCheckException;
import com.nhnacademy.front.account.customer.exception.CustomerLoginProcessingException;
import com.nhnacademy.front.account.customer.exception.CustomerPasswordCheckException;
import com.nhnacademy.front.account.customer.exception.CustomerRegisterProcessingException;
import com.nhnacademy.front.account.member.exception.DormantDoorayNotMatchedNumberException;
import com.nhnacademy.front.account.member.exception.DormantEmailNotMatchedNumberException;
import com.nhnacademy.front.account.member.exception.DormantProcessingException;
import com.nhnacademy.front.account.member.exception.GetMemberStateFailedException;
import com.nhnacademy.front.account.member.exception.LoginProcessException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberIdException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberInfoException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberRankNameException;
import com.nhnacademy.front.account.member.exception.RegisterNotEqualsPasswordException;
import com.nhnacademy.front.account.member.exception.RegisterProcessException;
import com.nhnacademy.front.account.memberrank.exception.NotFoundMemberRankException;
import com.nhnacademy.front.account.oauth.exception.PaycoProcessingException;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.common.error.exception.EmptyRequestException;
import com.nhnacademy.front.common.error.exception.EmptyResponseException;
import com.nhnacademy.front.common.error.exception.LoginRedirectException;
import com.nhnacademy.front.common.error.exception.ServerErrorException;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.product.like.exception.LikeProcessException;
import com.nhnacademy.front.review.exception.ReviewProcessException;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class WebAdviceController {
	private final ErrorMessageLoader errorMessageLoader;

	// @ExceptionHandler(LoginRedirectException.class)
	// public ModelAndView handleLoginRedirect(LoginRedirectException ex) {
	// 	ModelAndView modelAndView = new ModelAndView("redirect:/login");
	// 	modelAndView.addObject("error", ex.getMessage());
	// 	return modelAndView;
	// }

	// 잘못된 요청 에러
	@ExceptionHandler({RegisterNotEqualsPasswordException.class, GetAddressFailedException.class,
		SaveJwtTokenProcessException.class, EmptyResponseException.class, RegisterProcessException.class,
		EmptyRequestException.class, ValidationFailedException.class, LoginProcessException.class,
		SaveAddressFailedException.class, DeleteAddressFailedException.class, UpdateAddressFailedException.class,
		AdminSettingsFailedException.class, AdminSettingsMembersFailedException.class,
		AdminSettingsMemberUpdateFailedException.class, AdminSettingsMemberDeleteFailedException.class,
		PaycoProcessingException.class, CustomerLoginProcessingException.class,
		CustomerRegisterProcessingException.class,
		CustomerPasswordCheckException.class, GetMemberStateFailedException.class,
		CartProcessException.class, ReviewProcessException.class, LikeProcessException.class,
		DormantProcessingException.class, DormantDoorayNotMatchedNumberException.class,
		DormantEmailNotMatchedNumberException.class})
	public ResponseEntity<String> registerNotEqualsPasswordException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	// 찾지 못한 에러
	@ExceptionHandler({NotFoundMemberIdException.class, NotFoundMemberRankNameException.class,
		NotFoundMemberRankException.class, NotFoundMemberInfoException.class})
	public ResponseEntity<String> notFoundMemberIdException(Exception ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	// 500번 대 에러
	@ExceptionHandler({ServerErrorException.class})
	public ResponseEntity<Map<String, String>> handleSystemErrorException(Exception ex) {
		Map<String, String> body = new HashMap<>();
		body.put("errorMessage", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}

	// 인증 관련 에러
	@ExceptionHandler({CustomerLoginCheckException.class})
	public ResponseEntity<String> authException(Exception ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler({LoginRedirectException.class})
	public ModelAndView systemException500() {
		String code = "F500";
		String errorMessage = errorMessageLoader.getMessage(code);

		ModelAndView modelAndView = new ModelAndView("redirect:/error/500");
		modelAndView.addObject("errorMessage", errorMessage);

		return modelAndView;
	}

}
