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
import com.nhnacademy.front.account.member.exception.DormantProcessingException;
import com.nhnacademy.front.account.member.exception.GetMemberStateFailedException;
import com.nhnacademy.front.account.member.exception.LoginProcessException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberIdException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberInfoException;
import com.nhnacademy.front.account.member.exception.NotFoundMemberRankNameException;
import com.nhnacademy.front.account.member.exception.PasswordNotEqualsException;
import com.nhnacademy.front.account.member.exception.RegisterNotEqualsPasswordException;
import com.nhnacademy.front.account.member.exception.RegisterProcessException;
import com.nhnacademy.front.account.oauth.exception.PaycoProcessingException;
import com.nhnacademy.front.account.pointhistory.exception.PointHistoryGetException;
import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.common.error.exception.EmptyResponseException;
import com.nhnacademy.front.common.error.exception.LoginRedirectException;
import com.nhnacademy.front.common.error.exception.NotMatchedLoginPasswordException;
import com.nhnacademy.front.common.error.exception.ServerErrorException;
import com.nhnacademy.front.common.error.exception.ValidationFailedException;
import com.nhnacademy.front.common.error.loader.ErrorMessageLoader;
import com.nhnacademy.front.order.wrapper.exception.WrapperCreateProcessException;
import com.nhnacademy.front.order.wrapper.exception.WrapperGetProcessException;
import com.nhnacademy.front.order.wrapper.exception.WrapperUpdateProcessException;
import com.nhnacademy.front.product.category.exception.CategoryCreateProcessException;
import com.nhnacademy.front.product.category.exception.CategoryDeleteProcessException;
import com.nhnacademy.front.product.category.exception.CategoryGetProcessException;
import com.nhnacademy.front.product.category.exception.CategoryNotFoundException;
import com.nhnacademy.front.product.category.exception.CategoryUpdateProcessException;
import com.nhnacademy.front.product.like.exception.LikeProcessException;
import com.nhnacademy.front.product.product.exception.ProductCreateProcessException;
import com.nhnacademy.front.product.product.exception.ProductGetProcessException;
import com.nhnacademy.front.product.product.exception.ProductUpdateProcessException;
import com.nhnacademy.front.product.publisher.exception.PublisherCreateProcessException;
import com.nhnacademy.front.product.publisher.exception.PublisherGetProcessException;
import com.nhnacademy.front.product.publisher.exception.PublisherUpdateProcessException;
import com.nhnacademy.front.review.exception.ReviewProcessException;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class WebAdviceController {
	private static final String ERROR_MESSAGE = "errorMessage";

	private final ErrorMessageLoader errorMessageLoader;

	// 잘못된 요청 에러
	@ExceptionHandler({
		ValidationFailedException.class,
		CustomerRegisterProcessingException.class,
		CartProcessException.class, ReviewProcessException.class, LikeProcessException.class,
		DormantDoorayNotMatchedNumberException.class})
	public ResponseEntity<String> badRequestException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	// 찾지 못한 에러
	@ExceptionHandler({NotFoundMemberIdException.class})
	public ResponseEntity<String> notFoundMemberIdException(Exception ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	// 인증 관련 에러
	@ExceptionHandler({CustomerLoginCheckException.class, LoginRedirectException.class})
	public ModelAndView authException403() {
		String code = "A403";
		String errorMessage = errorMessageLoader.getMessage(code);

		ModelAndView modelAndView = new ModelAndView("redirect:/members/login");
		modelAndView.addObject(ERROR_MESSAGE, errorMessage);

		return modelAndView;
	}

	// 서버 관련 에러
	@ExceptionHandler({ServerErrorException.class, GetAddressFailedException.class, EmptyResponseException.class,
		SaveAddressFailedException.class, UpdateAddressFailedException.class, DeleteAddressFailedException.class,
		AdminSettingsFailedException.class, AdminSettingsMembersFailedException.class,
		AdminSettingsMemberUpdateFailedException.class,
		AdminSettingsMemberDeleteFailedException.class,
		SaveJwtTokenProcessException.class,
		DormantProcessingException.class, NotFoundMemberRankNameException.class, NotFoundMemberInfoException.class,
		RegisterProcessException.class, GetMemberStateFailedException.class, PaycoProcessingException.class,
		PointHistoryGetException.class,
		WrapperCreateProcessException.class, WrapperGetProcessException.class, WrapperUpdateProcessException.class,
		PublisherCreateProcessException.class, PublisherGetProcessException.class,
		PublisherUpdateProcessException.class,
		CategoryCreateProcessException.class, CategoryGetProcessException.class, CategoryNotFoundException.class,
		CategoryUpdateProcessException.class, CategoryDeleteProcessException.class,
		ProductCreateProcessException.class, ProductGetProcessException.class, ProductUpdateProcessException.class
	})
	public ModelAndView systemException500() {
		String code = "F500";
		String errorMessage = errorMessageLoader.getMessage(code);

		ModelAndView modelAndView = new ModelAndView("redirect:/error/500");
		modelAndView.addObject(ERROR_MESSAGE, errorMessage);

		return modelAndView;
	}

	@ExceptionHandler({LoginProcessException.class, NotMatchedLoginPasswordException.class})
	public ModelAndView memberLoginException() {
		String code = "M403";
		String errorMessage = errorMessageLoader.getMessage(code);

		ModelAndView modelAndView = new ModelAndView("redirect:/members/login");
		modelAndView.addObject(ERROR_MESSAGE, errorMessage);

		return modelAndView;
	}

	@ExceptionHandler({CustomerLoginProcessingException.class, CustomerPasswordCheckException.class})
	public ModelAndView customerLoginException() {
		String code = "C403";
		String errorMessage = errorMessageLoader.getMessage(code);

		ModelAndView modelAndView = new ModelAndView("redirect:/customers/login");
		modelAndView.addObject(ERROR_MESSAGE, errorMessage);

		return modelAndView;
	}

	@ExceptionHandler({RegisterNotEqualsPasswordException.class})
	public ModelAndView registerNotEqualsPasswordException() {
		String code = "R400";
		String errorMessage = errorMessageLoader.getMessage(code);

		ModelAndView modelAndView = new ModelAndView("redirect:/members/register");
		modelAndView.addObject(ERROR_MESSAGE, errorMessage);

		return modelAndView;
	}

	@ExceptionHandler({PasswordNotEqualsException.class})
	public ModelAndView memberInfoUpdatePasswordNotEqualsException() {
		String code = "R400";
		String errorMessage = errorMessageLoader.getMessage(code);

		ModelAndView modelAndView = new ModelAndView("redirect:/mypage/info");
		modelAndView.addObject(ERROR_MESSAGE, errorMessage);

		return modelAndView;
	}

	@SuppressWarnings("squid:S2699")
	@ExceptionHandler({Throwable.class})
	public ModelAndView throwableException() {
		String code = "F500";

		String errorMessage = errorMessageLoader.getMessage(code);

		ModelAndView modelAndView = new ModelAndView("redirect:/error/500");
		modelAndView.addObject(ERROR_MESSAGE, errorMessage);

		return modelAndView;
	}

}
