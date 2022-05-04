package com.numble.team3.like.annotation;

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
@ApiOperation(value = "좋아요 삭제")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "좋아요 삭제 성공",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 400,
      message = "좋아요 삭제 실패 \t\n 1. 존재하지 않는 좋아요",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\"message\" : \"존재하지 않는 좋아요입니다.\"}"))
    ),
    @ApiResponse(
      code = 401,
      message = "좋아요 삭제 실패 \t\n 1. access token이 유효하지 않음",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{}"))
    )
  }
)
@ApiImplicitParams(
  value = {
    @ApiImplicitParam(
      name = "Authorization",
      value = "access token",
      required = true,
      dataType = "String",
      paramType = "header"
    ),
  }
)
public @interface DeleteLikeSwagger {

}
