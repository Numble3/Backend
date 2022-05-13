package com.numble.team3.admin.annotation;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
@ApiOperation(value = "토큰 초기화")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "토큰 초기화 성공",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 401,
      message = "토큰 초기화 실패 \t\n 1. access token이 유효하지 않음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 403,
      message = "토큰 초기화 실패 \t\n 1. 권한 없음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    ),
  }
)
@ApiImplicitParams(
  value = {
    @ApiImplicitParam(
      name = "Authorization",
      value = "access token",
      required = true,
      dataTypeClass = String.class,
      paramType = "header"
    ),
  }
)
public @interface DeleteTokenSwagger {

}
