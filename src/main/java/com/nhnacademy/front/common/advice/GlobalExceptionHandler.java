package com.nhnacademy.front.common.advice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.nhnacademy.front.cart.exception.CartProcessException;
import com.nhnacademy.front.common.error.GlobalErrorResponse;
import com.nhnacademy.front.product.like.exception.LikeProcessException;
import com.nhnacademy.front.review.exception.ReviewProcessException;

/**
 * JSON 으로 반환해야하는 경우의 공통 예외처리 핸들러
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 서버에서의 에러 핸들러
	 */
	@ExceptionHandler({CartProcessException.class, ReviewProcessException.class, LikeProcessException.class})
	public ResponseEntity<GlobalErrorResponse> processException(Exception ex) {
		GlobalErrorResponse body = new GlobalErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}

	/**
	 * 파일 용량 초과 핸들러
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Map<String, Object>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("title", "파일 업로드 크기 초과");
		error.put("status", 413);
		error.put("timeStamp", LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
	}
	
}
