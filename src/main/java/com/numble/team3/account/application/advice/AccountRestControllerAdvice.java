package com.numble.team3.account.application.advice;

import com.numble.team3.account.controller.AccountController;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.account.AccountWithdrawalException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {AccountController.class})
public class AccountRestControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> methodArgumentNotValidExceptionHandler(BindException e) {
    return createResponse(e.getBindingResult().getFieldError().getDefaultMessage());
  }

  @ExceptionHandler(AccountNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> accountNotFoundExceptionHandler() {
    return createResponse("존재하지 않는 회원입니다.");
  }

  @ExceptionHandler(AccountWithdrawalException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> accountWithdrawalExceptionHandler() {
    return createResponse("이미 탈퇴처리 된 회원입니다.");
  }

  private Map<String, String> createResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return response;
  }
}
