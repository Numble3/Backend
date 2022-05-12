package com.numble.team3.video.annotation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation(value = "영상 상세 조회")
@ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "영상 상세 조회 성공"),
      @ApiResponse(code = 400, message = "영상 ID가 올바르지 않음")
    })
public @interface GetVideoDetailSwagger {}
