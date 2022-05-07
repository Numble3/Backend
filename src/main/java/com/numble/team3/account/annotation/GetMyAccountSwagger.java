package com.numble.team3.account.annotation;

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
@ApiOperation(value = "회원 정보 조회")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "회원 조회 성공",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{\n\"email\" : \"email\", \n\"profile\" : \"https://profile-url\", \n\"nickname\" : \"nickname\" \n}"))
    ),
    @ApiResponse(
      code = 401,
      message = "회원 조회 실패 \t\n 1. access token이 유효하지 않음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    )
  }
)
public @interface GetMyAccountSwagger {

}
