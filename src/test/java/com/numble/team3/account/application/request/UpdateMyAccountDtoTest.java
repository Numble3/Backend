package com.numble.team3.account.application.request;

import static com.numble.team3.factory.dto.AccountDtoFactory.createUpdateMyAccountDto;
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
@DisplayName("UpdateMyAccountDto Valid 테스트")
class UpdateMyAccountDtoTest {

  Validator validator;

  @BeforeEach
  void beforeEach() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 검증_성공_테스트() throws Exception {
    // given
    UpdateMyAccountDto dto = createUpdateMyAccountDto("profile", "nickname");

    // when
    Set<ConstraintViolation<UpdateMyAccountDto>> validate = validator.validate(dto);

    // then
    assertTrue(validate.isEmpty());
  }

  @Test
  void nickname_null_실패_테스트() throws Exception {
    // given
    UpdateMyAccountDto dto = createUpdateMyAccountDto("profile", null);

    // when
    ConstraintViolation<UpdateMyAccountDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("닉네임을 입력해주세요.", result.getMessage());
  }

  @Test
  void nickname_nullString_실패_테스트() throws Exception {
    // given
    UpdateMyAccountDto dto = createUpdateMyAccountDto("profile", "");

    // when
    ConstraintViolation<UpdateMyAccountDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("닉네임을 입력해주세요.", result.getMessage());
  }
}