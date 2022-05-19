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
@ApiOperation(value = "일일 랭킹 전체 조회")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "랭킹 조회 성공",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\n\"contents\" : [ \n\t { \n\t\t \"createdAt\" : 영상 등록일(yyyy-MM-dd), \n\t\t \"like\" : 좋아요 수, \n\t\t \"nickname\" : \"비디오 업로더 닉네임\", \n\t\t \"profileUrl\" : \"비디오 업로더 프로필 경로\", \n\t\t \"thumbnailPath\" : \"영상 썸네일 경로\", \n\t\t \"title\" : \"영상 제목\", \n\t\t \"videoId\" : 영상 ID, \n\t\t \"view\" : 조회수, \n\t\t \"category\" : \"영상 카테고리\", \n\t\t \"videoType\" : \"영상 타입(EMBEDDED, VIDEO)\" \n\t } \n\t ] \n}"))
    )
  }
)
public @interface GetVideoRankByDayLikeSwagger {

}
