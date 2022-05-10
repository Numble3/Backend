package com.numble.team3.like.application.advice;

import com.numble.team3.exception.like.LikeVideoNotFoundException;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.like.controller.LikeVideoController;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(assignableTypes = {LikeVideoController.class})
public class LikeVideoRestControllerAdvice {

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> missingServletRequestParameterException() {
    return createResponse("쿼리 파라미터를 확인해주세요.");
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> methodArgumentTypeMismatchException() {
    return createResponse("쿼리 파라미터의 타입을 확인해주세요.");
  }

  @ExceptionHandler(LikeVideoNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> likeNotFoundExceptionHandler() {
    return createResponse("존재하지 않는 좋아요입니다.");
  }

  @ExceptionHandler(VideoNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> videoNotFoundExceptionHandler() {
    return createResponse("존재하지 않는 비디오입니다.");
  }

  private Map<String, String> createResponse(String message) {
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return response;
  }
}
