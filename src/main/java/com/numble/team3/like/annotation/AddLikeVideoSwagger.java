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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation(value = "좋아요 추가")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 201,
      message = "좋아요 추가 성공",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\"nowLikeCount\" : 해당 비디오의 현재 좋아요 수}"))
    ),
    @ApiResponse(
      code = 400,
      message = "좋아요 추가 실패 \t\n 1. 존재하지 않는 비디오 \t\n 2. 쿼리 파라미터가 존재하지 않음 \t\n 3. 쿼리 파라미터의 타입이 올바르지 않음 \t\n 4. 이미 관심영상(좋아요)에 추가한 영상",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\n\"message\" : \"존재하지 않는 비디오입니다.\"\n\"message\" : \"쿼리 파라미터를 확인해주세요.\"\n\"message\" : \"쿼리 파라미터의 타입을 확인해주세요.\" \n\"message\" : \"이미 관심 영상(좋아요)에 추가한 영상입니다.\"\n}"))
    ),
    @ApiResponse(
      code = 401,
      message = "좋아요 추가 실패 \t\n 1. access token이 유효하지 않음",
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
      dataTypeClass = String.class,
      paramType = "header"
    ),
  }
)
@ResponseStatus(HttpStatus.CREATED)
public @interface AddLikeVideoSwagger {

}
