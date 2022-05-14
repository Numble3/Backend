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
      code = 401,
      message = "access token 재발급 실패 \n\t 1. Authorization 헤더가 존재하지 않음 \n\t 2. Authorization 헤더로 전달한 토큰이 유효하지 않음(refresh token까지 만료된 상태로, 재 로그인 필요)",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    )
  }
)
public @interface CreateAccessTokenSwagger {

}
