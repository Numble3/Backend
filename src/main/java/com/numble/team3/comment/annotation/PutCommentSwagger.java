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
@ApiOperation(value = "댓글 수정 api (로그인 필요)")
@ApiResponses(
  value = {
    @ApiResponse(code = 204, message = "댓글 수정 성공"),
    @ApiResponse(code = 400, message = "요청 데이터가 올바르지 않음"),
    @ApiResponse(code = 401, message = "허가받지 않은 요청")
  })
public @interface PutCommentSwagger {

}
