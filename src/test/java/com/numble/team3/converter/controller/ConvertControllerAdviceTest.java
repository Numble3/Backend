package com.numble.team3.converter.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.numble.team3.converter.application.ImageConvertService;
import com.numble.team3.converter.application.advice.ConvertRestControllerAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("ConvertRestControllerAdvice 이미지 기능 테스트")
public class ConvertControllerAdviceTest {

  @Mock
  ImageConvertService imageConvertService;

  @InjectMocks
  ConvertController convertController;

  MockMvc mockMvc;

  @BeforeEach
  void BeforeEach() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(convertController)
      .setControllerAdvice(new ConvertRestControllerAdvice())
      .alwaysDo(print())
      .build();
  }

  @Test
  void imageResize_type_미지원_실패_테스트() throws Exception {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});

    // when
    ResultActions result = mockMvc.perform(
      multipart("/api/images/resize")
        .file(file)
        .param("width", "360")
        .param("height", "360")
        .param("type", "없는 방식")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("지원하지 않는 리사이즈 방식입니다."));
  }

  @Test
  void imageResize_thumbnail_비율_실패_테스트() throws Exception {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});

    // when
    ResultActions result = mockMvc.perform(
      multipart("/api/images/resize")
        .file(file)
        .param("width", "360")
        .param("height", "36")
        .param("type", "profile")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("잘못된 이미지 비율입니다."));
  }
}