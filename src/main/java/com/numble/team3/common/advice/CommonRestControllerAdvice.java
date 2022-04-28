package com.numble.team3.common.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonRestControllerAdvice {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> httpMessageNotReadableException() {
    return createResponse("json 형식을 올바르게 작성해주세요.");
  }

  private Map<String, String> createResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return response;
  }
}
