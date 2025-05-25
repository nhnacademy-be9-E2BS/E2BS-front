package com.nhnacademy.front.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.nhnacademy.front.account.address.exception.DeleteAddressFailedException;
import com.nhnacademy.front.account.address.exception.GetAddressFailedException;
import com.nhnacademy.front.account.address.exception.SaveAddressFailedException;
import com.nhnacademy.front.account.address.exception.UpdateAddressFailedException;
import com.nhnacademy.front.account.auth.exception.SaveJwtTokenProcessException;
import com.nhnacademy.front.account.member.exception.LoginProcessException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberIdException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberInfoException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberRankNameException;
import com.nhnacademy.front.account.member.exception.RegisterNotEqualsPasswordException;
import com.nhnacademy.front.account.member.exception.RegisterProcessException;
import com.nhnacademy.front.account.memberrank.exception.NotFoundMemberRankException;
import com.nhnacademy.front.common.exception.EmptyRequestException;
import com.nhnacademy.front.common.exception.EmptyResponseException;
import com.nhnacademy.front.common.exception.LoginRedirectException;
import com.nhnacademy.front.common.exception.ValidationFailedException;

@ControllerAdvice
public class WebAdviceController {

	@ExceptionHandler(LoginRedirectException.class)
	public ModelAndView handleLoginRedirect(LoginRedirectException ex) {
		ModelAndView modelAndView = new ModelAndView("redirect:/login");
		modelAndView.addObject("error", ex.getMessage());
		return modelAndView;
	}

	@ExceptionHandler({RegisterNotEqualsPasswordException.class, GetAddressFailedException.class,
		SaveJwtTokenProcessException.class, EmptyResponseException.class, RegisterProcessException.class,
		EmptyRequestException.class, ValidationFailedException.class, LoginProcessException.class,
		SaveAddressFailedException.class, DeleteAddressFailedException.class, UpdateAddressFailedException.class})
	public ResponseEntity<String> registerNotEqualsPasswordException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler({NotFoundMemberIdException.class, NotFoundMemberRankNameException.class,
		NotFoundMemberRankException.class, NotFoundMemberInfoException.class})
	public ResponseEntity<String> notFoundMemberIdException(Exception ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

}
