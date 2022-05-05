package com.numble.team3.converter.application.advice;

import com.numble.team3.exception.convert.ImageConvertFailureException;
import com.numble.team3.exception.convert.ImageResizeTypeUnSupportException;
import com.numble.team3.exception.convert.ImageTypeUnSupportException;
import com.numble.team3.converter.controller.ConvertController;
import com.numble.team3.exception.image.ImageWrongRatioException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ConvertController.class})
public class ConvertRestControllerAdvice {

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

  @ExceptionHandler(ImageWrongRatioException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> imageWrongRatioExceptionHandler() {
    return createResponse("잘못된 이미지 비율입니다.");
  }

  private Map<String, String> createResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return response;
  }
}
