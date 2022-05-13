package com.numble.team3.sign.annotation;

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
@ApiOperation(value = "access token 재발급")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "access token 재발급 성공",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{\"accessToken\" : \"String\", \"refreshToken\" : \"String\"}"))
    ),
    @ApiResponse(
      code = 400,
      message = "요청 시 아무 쿠키도 전달되지 않음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{\"message\" : \"refreshToken 쿠키가 누락되었습니다.\"}"))
    ),
    @ApiResponse(
      code = 401,
      message = "refreshToken 쿠키 누락",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{\"message\" : \"refreshToken 쿠키가 누락되었습니다.\"}"))
    )
  }
)
public @interface CreateAccessTokenSwagger {

}
