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
@ApiOperation(value = "회원 목록 조회")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "회원 목록 조회 성공",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{ \n\"accountDtos\" : [ \n\t { \n\t\t \"id\" : account id, \n\t\t \"email\" : \"email\", \n\t\t \"nickname\" : \"nickname\", \n\t\t \"lastLogin\" : \"yyyy-MM-dd\", \n\t\t \"createdAt\" : \"yyyy-MM-dd\" \n\t } \n\t ], \n\t \"totalCount\" : 전체 회원 수, \n\t \"nowPage\" : 현재 페이지, \n\t \"totalPage\" : 전체 페이지 , \n\t \"size\" : 페이지 크기 \n}"))
    ),
    @ApiResponse(
      code = 400,
      message = "회원 목록 조회 실패 \t\n 1. 올바르지 않은 페이지 번호",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{\"message\" : \"올바르지 않은 페이지 번호 요청입니다.\"}"))
    ),
    @ApiResponse(
      code = 401,
      message = "회원 목록 조회 실패 \t\n 1. access token이 유효하지 않음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 403,
      message = "회원 목록 조회 실패 \t\n 1. 권한 없음",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    ),
  }
)
public @interface GetAccountsSwagger {

}
