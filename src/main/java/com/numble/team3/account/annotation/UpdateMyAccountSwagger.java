package com.numble.team3.account.annotation;

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
@ApiOperation(value = "회원 정보 수정")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "회원 정보 수정 성공",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 400,
      message = "회원 정보 수정 실패 \t\n 1. nickname 누락 \t\n 2. nickname 중복",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\n\"message\" : \"닉네임을 입력해주세요.\"\n\"message\" : \"이미 존재하는 닉네임입니다.\"\n}"))
    ),
    @ApiResponse(
      code = 401,
      message = "회원 정보 수정 실패 \t\n 1. access token이 유효하지 않음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    )
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
public @interface UpdateMyAccountSwagger {

}
