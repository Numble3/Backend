package com.numble.team3.common.advice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

@RestControllerAdvice
@Slf4j
public class CommonRestControllerAdvice {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> exceptionHandler(Exception e) {
    return createResponse(e.toString());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> httpMessageNotReadableException() {
    return createResponse("json 형식을 올바르게 작성해주세요.");
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleConstraintViolationException(
      final MethodArgumentNotValidException ex, final ServletWebRequest request) {
    final List<InvalidParameterDto> invalidParameters =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    InvalidParameterDto.builder()
                        .parameter(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
            .collect(Collectors.toList());
    Map<String, Object> response = new HashMap<>();
    response.put("message", "파라미터 형식이 잘못되었습니다.");
    response.put("invalidParameters", invalidParameters);
    return response;
  }

  private Map<String, String> createResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return response;
  }
}
