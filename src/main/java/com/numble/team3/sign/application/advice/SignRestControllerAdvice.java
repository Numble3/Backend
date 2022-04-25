package com.numble.team3.sign.application.advice;

import com.numble.team3.exception.sign.SignInFailureException;
import com.numble.team3.exception.sign.TokenFailureException;
import com.numble.team3.sign.controller.SignController;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {SignController.class})
public class SignRestControllerAdvice {

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> bindException(BindException e) {
    return createResponse(e.getBindingResult().getFieldError().getDefaultMessage());
  }

  @ExceptionHandler(TokenFailureException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  Map<String, String> tokenFailureExceptionHandler() {
    return createResponse("유효하지 않은 토큰입니다.");
  }

  @ExceptionHandler(SignInFailureException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  Map<String, String> signInFailureException() {
    return createResponse("로그인에 실패했습니다.");
  }

  private Map<String, String> createResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return response;
  }
}
