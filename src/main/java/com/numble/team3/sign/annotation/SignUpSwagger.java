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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiOperation(value = "회원 가입")
@ApiResponses(
  value = {
    @ApiResponse(
      code = 201,
      message = "회원가입 성공",
      examples = @Example(@ExampleProperty(mediaType= "application/json", value = "{}"))
    ),
    @ApiResponse(
      code = 400,
      message = "회원가입 요청 데이터가 올바르지 않음 \t\n 1. 이메일을 입력하지 않음 \t\n 2. 입력한 이메일이 이메일 형식이 아님 \t\n 3. 비밀번호를 입력하지 않음 \t\n 4. 닉네임을 입력하지 않음",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\n\"message\" : \"이메일을 입력해주세요.\" \n\"message\" : \"이메일 형식으로 입력해주세요.\" \n\"message\" : \"비밀번호를 입력해주세요.\" \n\"message\" : \"닉네임을 입력해주세요.\"\n}"))
    ),
    @ApiResponse(
      code = 409,
      message = "회원가입 요청 데이터가 올바르지 않음 \t\n 1. 이메일 중복 \t\n 2. 닉네임 중복",
      examples = @Example(@ExampleProperty(mediaType = "application/json", value = "{\n\"message\" : \"이미 존재하는 이메일입니다.\" \n\"message\" : \"이미 존재하는 닉네임입니다.\"\n}"))
    )
  }
)
@ResponseStatus(HttpStatus.CREATED)
public @interface SignUpSwagger {

}
