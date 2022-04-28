package com.numble.team3.sign.application.advice;

import com.numble.team3.exception.account.AccountEmailAlreadyExistsException;
import com.numble.team3.exception.account.AccountNicknameAlreadyExistsException;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.account.AccountWithdrawalException;
import com.numble.team3.exception.sign.SignInFailureException;
import com.numble.team3.exception.sign.TokenFailureException;
import com.numble.team3.sign.controller.SignController;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {SignController.class})
public class SignRestControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> methodArgumentNotValidExceptionHandler(BindException e) {
    return createResponse(e.getBindingResult().getFieldError().getDefaultMessage());
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> missingRequestHeaderExceptionHandler() {
    return createResponse("Authorization 헤더가 누락되었습니다.");
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

  @ExceptionHandler(AccountEmailAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  Map<String, String> accountEmailAlreadyExistsExceptionHandler() {
    return createResponse("이미 존재하는 이메일입니다.");
  }

  @ExceptionHandler(AccountNicknameAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  Map<String, String> accountNicknameAlreadyExistsExceptionHandler() {
    return createResponse("이미 존재하는 닉네임입니다.");
  }

  @ExceptionHandler(AccountNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> accountNotFoundExceptionHandler() {
    return createResponse("존재하지 않는 이메일입니다.");
  }

  @ExceptionHandler(AccountWithdrawalException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> accountWithdrawalExceptionHandler() {
    return createResponse("이미 탈퇴처리 된 계정입니다.");
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String>  nullPointerException() {
    return createResponse("refreshToken 쿠키가 누락되었습니다.");
  }

  private Map<String, String> createResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return response;
  }
}
