package com.numble.team3.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.numble.team3.sign.application.request.SignInDto;
import com.numble.team3.sign.application.request.SignUpDto;
import java.lang.reflect.Constructor;

public class SignDtoFactory {

  public static SignUpDto createSignUpDto(String email, String password, String nickname) throws Exception {
    Constructor<SignUpDto> constructor = SignUpDto.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    SignUpDto dto = constructor.newInstance();
    setField(dto, "email", email);
    setField(dto, "password", password);
    setField(dto, "nickname", nickname);

    return dto;
  }

  public static SignInDto createSignInDto(String email, String password) throws Exception {
    Constructor<SignInDto> constructor = SignInDto.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    SignInDto dto = constructor.newInstance();
    setField(dto, "email", email);
    setField(dto, "password", password);

    return dto;
  }
}
