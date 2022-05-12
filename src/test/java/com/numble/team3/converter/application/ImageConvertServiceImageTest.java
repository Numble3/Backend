package com.numble.team3.converter.application;

import static com.numble.team3.factory.dto.ImageDtoFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import com.numble.team3.converter.application.request.CreateImageDto;
import com.numble.team3.converter.infra.AwsConvertImageUtils;
import com.numble.team3.exception.convert.ImageTypeUnSupportException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("ConvertService 이미지 기능 테스트")
class ImageConvertServiceImageTest {

  @Mock
  AwsConvertImageUtils imageUtils;

  ImageConvertService imageConvertService;

  @BeforeEach
  void beforeEach() {
    imageConvertService = new ApiGatewayImageConvertService(imageUtils);
  }

  @Test
  void uploadResizeImage_성공_테스트() throws IOException {
    // given
    MockMultipartFile file =
        new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[] {1});
    CreateImageDto dto = createImageDto(file, "360", "360", "profile");

    willDoNothing()
        .given(imageUtils)
        .saveTempImageFileForMetadata(anyString(), any(CreateImageDto.class));
    given(imageUtils.processImageResize(anyString(), any(CreateImageDto.class)))
        .willReturn("파일 url");

    // when
    String url = imageConvertService.uploadResizeImage(dto);

    // then
    assertEquals("파일 url", url);
  }

  @Test
  void uploadResizeImage_file_타입_미지원_실패_테스트() {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.gif", "image/gif", new byte[] {1});
    CreateImageDto dto = createImageDto(file, "360", "360", "profile");

    // when, then
    assertThrows(
        ImageTypeUnSupportException.class, () -> imageConvertService.uploadResizeImage(dto));
  }

  @Test
  void uploadResizeImage_saveTempVideoForConvert_실패_테스트() throws IOException {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.gif", "image/gif", new byte[] {1});
    CreateImageDto dto = createImageDto(file, "360", "360", "profile");

    // when, then
    assertThrows(
        ImageTypeUnSupportException.class, () -> imageConvertService.uploadResizeImage(dto));
  }
}
