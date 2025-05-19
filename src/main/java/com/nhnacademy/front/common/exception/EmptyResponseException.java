package com.nhnacademy.front.common.exception;

public class EmptyResponseException extends RuntimeException {
  public EmptyResponseException(String message) {
    super(message);
  }
}
