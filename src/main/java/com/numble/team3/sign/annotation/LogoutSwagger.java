package com.numble.team3.sign.annotation;

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
@ApiOperation(value = "로그아웃")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "로그아웃 성공 \t\n refreshToken 쿠키 삭제",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 401,
      message = "로그아웃 실패 \t\n 1. access token이 유효하지 않음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    )
  }
)
public @interface LogoutSwagger {

}
