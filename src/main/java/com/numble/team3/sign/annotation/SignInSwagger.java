package com.numble.team3.sign.annotation;

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
@ApiOperation(value = "로그인")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 200,
      message = "로그인 성공 \t\n refreshToken 쿠키 생성"
    ),
    @ApiResponse(
      code = 400,
      message = "로그인 요청 데이터가 올바르지 않음 \t\n 1. 이메일을 입력하지 않음 \t\n 2. 입력한 이메일이 이메일 형식이 아님 \t\n 3. 비밀번호를 입력하지 않음 \t\n 4. 이미 탈퇴된 이메일로 로그인 시도",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\n\"message\" : \"이메일을 입력해주세요.\", \n\"message\" : \"이메일 형식으로 입력해주세요.\" \n\"message\" : \"비밀번호를 입력해주세요.\" \n\"message\" : \"이미 탈퇴처리 된 계정입니다.\"\n}"))
    ),
    @ApiResponse(
      code = 409,
      message = "로그인 실패 \t\n 1. 일치하는 이메일이 존재하지 않음 \t\n 2. 해당 이메일의 비밀번호가 일치하지 않음",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\"message\" : \"로그인에 실패했습니다.\"}"))
    )
  }
)
public @interface SignInSwagger {

}
