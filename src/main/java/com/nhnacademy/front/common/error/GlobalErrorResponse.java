package com.nhnacademy.front.common.error;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record GlobalErrorResponse(String title, int status,
	                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timeStamp) {
}
