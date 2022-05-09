package com.numble.team3.sign.application.request;

import static com.numble.team3.factory.dto.SignDtoFactory.createSignUpDto;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("SignUpDto Valid 테스트")
class SignUpDtoTest {

  Validator validator;

  @BeforeEach
  void beforeEach() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 검증_성공_테스트() throws Exception {
    // given
    SignUpDto dto = createSignUpDto("test@email.com", "1234", "nickname");

    // when
    Set<ConstraintViolation<SignUpDto>> validate = validator.validate(dto);

    // then
    assertTrue(validate.isEmpty());
  }

  @Test
  void email_null_실패_테스트() throws Exception {
    // given
    SignUpDto dto = createSignUpDto(null, "1234", "nickname");

    // when
    ConstraintViolation<SignUpDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("이메일을 입력해주세요.", result.getMessage());
  }

  @Test
  void email_nullString_실패_테스트() throws Exception {
    // given
    SignUpDto dto = createSignUpDto("", "1234", "nickname");

    // when
    ConstraintViolation<SignUpDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("이메일을 입력해주세요.", result.getMessage());
  }

  @Test
  void email_형식_실패_테스트() throws Exception {
    // given
    SignUpDto dto = createSignUpDto("asdf", "1234", "nickname");

    // when
    ConstraintViolation<SignUpDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("이메일 형식으로 입력해주세요.", result.getMessage());
  }

  @Test
  void password_null_실패_테스트() throws Exception {
    // given
    SignUpDto dto = createSignUpDto("test@email.com", null, "nickname");

    // when
    ConstraintViolation<SignUpDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("비밀번호를 입력해주세요.", result.getMessage());
  }

  @Test
  void password_nullString_실패_테스트() throws Exception {
    // given
    SignUpDto dto = createSignUpDto("test@email.com", "", "nickname");

    // when
    ConstraintViolation<SignUpDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("비밀번호를 입력해주세요.", result.getMessage());
  }

  @Test
  void nickname_null_실패_테스트() throws Exception {
    // given
    SignUpDto dto = createSignUpDto("test@email.com", "1234", null);

    // when
    ConstraintViolation<SignUpDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("닉네임을 입력해주세요.", result.getMessage());
  }

  @Test
  void nickname_nullString_실패_테스트() throws Exception {
    // given
    SignUpDto dto = createSignUpDto("test@email.com", "1234", "");

    // when
    ConstraintViolation<SignUpDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("닉네임을 입력해주세요.", result.getMessage());
  }
}