package com.numble.team3.video.annotation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation(value = "영상 업로드 (로그인 필요)")
@ApiResponses(
    value = {
      @ApiResponse(code = 201, message = "영상 업로드 성공"),
      @ApiResponse(
          code = 400,
          message =
              "영상 업로드 요청 데이터가 올바르지 않음 "
                  + "\t\n 1.영상 제목은 반드시 있어야 합니다. "
                  + "\t\n 2.영상 내용은 반드시 있어야 합니다. "
                  + "\t\n 3.썸네일 경로는 반드시 있어야 합니다."
                  + "\t\n 4.영상 길이는 최소 0보다 커야 합니다."),
    })
public @interface CreateVideoSwagger {}
