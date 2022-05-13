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
@ApiOperation(value = "회원 단일 조회")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "회원 단일 조회 성공",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\n\"id\" : 회원 ID, \n\"email\" : \"회원 이메일\", \n\"nickname\" : \"회원 닉네임\", \n\"lastLogin\" : \"마지막 로그인 날짜(yyyy-MM-dd)\", \n\"createdAt\" : \"회원가입일(yyyy-MM-dd)\" \n}"))
    ),
    @ApiResponse(
      code = 400,
      message = "회원 단일 조회 실패 \t\n 1. 존재하지 않는 회원",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{\"message\" : \"존재하지 않는 회원입니다.\"}"))
    ),
    @ApiResponse(
      code = 401,
      message = "회원 단일 조회 실패 \t\n 1. access token이 유효하지 않음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 403,
      message = "회원 단일 조회 실패 \t\n 1. 권한 없음",
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
public @interface GetAccountSwagger {

}
