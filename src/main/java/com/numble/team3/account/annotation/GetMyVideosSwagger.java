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
@ApiOperation(value = "업로드한 비디오 조회")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "비디오 조회 성공",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\n \"videos\" : [ \n\t { \n\t \"videoId\" : 영상 ID, \n\t \"thumbnailPath\" : \"Thumbnail 경로\", \n\t \"title\" : \"영상 제목\", \n\t \"nickname\" : \"영상 업로더 닉네임\", \n\t \"view\" : 조회수, \n\t \"like\" : 좋아요 수, \n\t \"createdAt\" : \"업로드 날짜\", \n\t \"videoType\" : \"영상 타입(임베딩 영상, 직접 업로드)\" \n\t } \n ] \n \"lastVideoId\" :  다음 페이지 요청을 위한 videoId \n}"))
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
public @interface GetMyVideosSwagger {

}
