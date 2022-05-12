package com.numble.team3.comment.annotation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation(value = "댓글 목록 조회 api")
@ApiResponses(
  value = {
    @ApiResponse(code = 200, message = "댓글 목록 조회 성공"),
    @ApiResponse(code = 401, message = "허가받지 않은 요청")
  })
public @interface GetAllCommentSwagger {

}
