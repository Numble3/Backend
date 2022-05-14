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
@ApiOperation(value = "업로드한 비디오 조회")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "비디오 조회 성공",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{ \n\"videos\" : [ \n\t { \n\t\t \"videoId\" : 영상 ID, \n\t\t \"thumbnailUrl\" : \"썸네일 경로\", \n\t\t \"title\" : \"영상 제목\" \n\t } \n\t ], \n\t \"totalCount\" : 전체 업로드 수, \n\t \"nowPage\" : 현재 페이지, \n\t \"totalPage\" : 전체 페이지 , \n\t \"size\" : 페이지 크기 \n}"))
    ),
    @ApiResponse(
      code = 401,
      message = "비디오 조회 실패 \t\n 1. Authorization 헤더 누락 2. access token이 유효하지 않음",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 403,
      message = "비디오 조회 실패 \t\n 1. 권한 부족(관리자 로그인이 아닌 상태)",
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
public @interface GetAccountVideosSwagger {

}
