package com.numble.team3.image.application.advice;

import com.numble.team3.exception.image.ImageConvertFailureException;
import com.numble.team3.exception.image.ImageResizeTypeUnSupportException;
import com.numble.team3.exception.image.ImageTypeUnSupportException;
import com.numble.team3.image.controller.ImageController;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ImageController.class})
public class ImageRestControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> methodArgumentNotValidExceptionHandler(BindException e) {
    return createResponse(e.getBindingResult().getFieldError().getDefaultMessage());
  }

  @ExceptionHandler(ImageResizeTypeUnSupportException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> imageResizeTypeUnSupportException() {
    return createResponse("지원하지 않는 리사이즈 방식입니다.");
  }

  @ExceptionHandler(ImageTypeUnSupportException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> imageTypeUnSupportExceptionHandler() {
    return createResponse("지원하지 않는 이미지 파일입니다.");
  }

  @ExceptionHandler(ImageConvertFailureException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  Map<String, String> imageConvertFailureExceptionHandler(BindException e) {
    return createResponse("이미지 변환에 실패했습니다.");
  }

  private Map<String, String> createResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return response;
  }
}
