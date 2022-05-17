package com.numble.team3.video.advice;

import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.video.controller.VideoController;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(assignableTypes = {VideoController.class})
public class VideoRestControllerAdvice {
  @ExceptionHandler(VideoNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> videoNotFoundExceptionHandler() {
    return createResponse("존재하지 않는 비디오입니다.");
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> methodArgumentTypeMismatchException() {
    return createResponse("쿼리 파라미터의 타입을 확인해주세요.");
  }


  private Map<String, String> createResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return response;
  }
}
