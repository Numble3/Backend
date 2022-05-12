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
@ApiOperation(value = "영상 목록 조회")
@ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "영상 목록 조회 성공"),
    })
public @interface GetVideoAllSwagger {}
