package com.numble.team3.converter.application.request;

import static org.junit.jupiter.api.Assertions.*;

import com.numble.team3.factory.dto.ImageDtoFactory;
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
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("CreateImageDto Valid 테스트")
class CreateImageDtoTest {

  Validator validator;

  @BeforeEach
  void beforeEach() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 검증_성공_테스트() {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});
    CreateImageDto dto = ImageDtoFactory.createImageDto(file, "360", "360", "profile");

    // when
    Set<ConstraintViolation<CreateImageDto>> validate = validator.validate(dto);

    // then
    assertTrue(validate.isEmpty());
  }

  @Test
  void file_NotNull_실패_테스트() {
    // given
    CreateImageDto dto = ImageDtoFactory.createImageDto(null, "360", "360", "profile");

    // when
    ConstraintViolation<CreateImageDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("업로드할 이미지 파일을 선택해주세요.", result.getMessage());
  }

  @Test
  void width_NotBlank_null_실패_테스트() {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});
    CreateImageDto dto = ImageDtoFactory.createImageDto(file, null, "360", "profile");

    // when
    ConstraintViolation<CreateImageDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("리사이즈할 가로 길이를 입력해주세요.", result.getMessage());
  }

  @Test
  void width_NotBlank_nullString_실패_테스트() {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});
    CreateImageDto dto = ImageDtoFactory.createImageDto(file, "", "360", "profile");

    // when
    ConstraintViolation<CreateImageDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("리사이즈할 가로 길이를 입력해주세요.", result.getMessage());
  }

  @Test
  void height_NotBlank_null_실패_테스트() {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});
    CreateImageDto dto = ImageDtoFactory.createImageDto(file, "360", null, "profile");

    // when
    ConstraintViolation<CreateImageDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("리사이즈할 세로 길이를 입력해주세요.", result.getMessage());
  }

  @Test
  void height_NotBlank_nullString_실패_테스트() {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});
    CreateImageDto dto = ImageDtoFactory.createImageDto(file, "360", "", "profile");

    // when
    ConstraintViolation<CreateImageDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("리사이즈할 세로 길이를 입력해주세요.", result.getMessage());
  }

  @Test
  void type_NotBlank_null_실패_테스트() {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});
    CreateImageDto dto = ImageDtoFactory.createImageDto(file, "360", "360", null);

    // when
    ConstraintViolation<CreateImageDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("리사이즈할 타입을 입력해주세요.", result.getMessage());
  }

  @Test
  void type_NotBlank_nullString_실패_테스트() {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});
    CreateImageDto dto = ImageDtoFactory.createImageDto(file, "360", "360", "");

    // when
    ConstraintViolation<CreateImageDto> result = validator.validate(dto).iterator().next();

    // then
    assertEquals("리사이즈할 타입을 입력해주세요.", result.getMessage());
  }
}