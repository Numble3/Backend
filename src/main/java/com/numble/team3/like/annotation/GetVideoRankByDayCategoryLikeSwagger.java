package com.numble.team3.like.annotation;

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
@ApiOperation(value = "일일 랭킹 카테고리 별 조회")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "랭킹 조회 성공",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\"카테고리 명\" : [ \n\t { \n\t\t \"videoDto\" : { \n\t\t\t \"videoId\" : 영상 ID, \n\t\t\t \"thumbnailPath\" : \"썸네일 경로\", \n\t\t\t \"title\" : \"영상 제목\", \n\t\t\t \"nickname\" : \"비디오 업로더 닉네임\", \n\t\t\t \"view\" : 조회수, \n\t\t\t \"like\" : 좋아요 수, \n\t\t\t \"createdAt\" : \"영상 업로드 날짜(yyyy-MM-dd)\" \n\t\t } \n\t } \n]\n}"))
    )
  }
)
public @interface GetVideoRankByDayCategoryLikeSwagger {

}
