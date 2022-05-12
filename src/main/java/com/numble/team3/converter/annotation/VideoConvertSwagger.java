package com.numble.team3.converter.annotation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation(value = "동영상 변환")
@ApiResponses(
    value = {
      @ApiResponse(
          code = 201,
          message = "동영상 변환 성공",
          examples =
              @Example(
                  @ExampleProperty(
                      mediaType = "application/json",
                      value = "{\"url\" : \"https://video_url\" \t\n \"duration\" : 123}"))),
      @ApiResponse(code = 400, message = "동영상 변환 실패 \t\n 1. 지원하지 않는 동영상 타입 \t\n 2. 동영상 크기 제한 초과"),
      @ApiResponse(
          code = 401,
          message = "회원 인증 실패 \t\n 1. access token이 유효하지 않음",
          examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{}"))),
      @ApiResponse(
          code = 500,
          message = "동영상 변환 실패 \t\n 1. aws 문제",
          examples =
              @Example(
                  @ExampleProperty(
                      mediaType = "application/json",
                      value = "{\"message\" : \"동영상 변환에 실패했습니다.\"}")))
    })
public @interface VideoConvertSwagger {}
