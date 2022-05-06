package com.numble.team3.admin.annotation;

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
@ApiOperation(value = "회원 강제 탈퇴")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "회원 강제 탈퇴 성공",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 400,
      message = "회원 강제 탈퇴 실패 \t\n 1. 존재하지 않는 회원 id",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\"message\" : \"존재하지 않는 회원 id입니다.\"}"))
    ),
    @ApiResponse(
      code = 401,
      message = "회원 강제 탈퇴 실패 \t\n 1. access token이 유효하지 않음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 403,
      message = "회원 강제 탈퇴 실패 \t\n 1. 권한 없음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    ),
  }
)
public @interface WithdrawalSwagger {

}
