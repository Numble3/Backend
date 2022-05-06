package com.numble.team3.admin.application.advice;

import com.numble.team3.admin.controller.AdminAccountController;
import com.numble.team3.exception.account.AccountNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {AdminAccountController.class})
public class AdminAccountRestControllerAdvice {

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> illegalArgumentExceptionHandler() {
    return createResponse("올바르지 않은 페이지 번호 요청입니다.");
  }

  @ExceptionHandler(AccountNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> accountNotFoundExceptionHandler() {
    return createResponse("존재하지 않는 회원입니다.");
  }

  private Map<String, String> createResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return response;
  }
}