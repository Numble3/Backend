package com.numble.team3.converter.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.numble.team3.converter.application.ConvertService;
import com.numble.team3.converter.application.request.CreateImageDto;
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
@DisplayName("ConvertController 이미지 기능 테스트")
class ConvertControllerImageTest {

  @Mock
  ConvertService convertService;

  @InjectMocks
  ConvertController convertController;

  MockMvc mockMvc;

  @BeforeEach
  void BeforeEach() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(convertController)
      .alwaysDo(print())
      .build();
  }

  @Test
  void imageResize_profile_테스트() throws Exception {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});

    given(convertService.uploadResizeImage(any(CreateImageDto.class))).willReturn("profile url");

    // when
    ResultActions result = mockMvc.perform(
      multipart("/api/images/resize")
        .file(file)
        .param("width", "360")
        .param("height", "360")
        .param("type", "profile")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.url").value("profile url"));
  }

  @Test
  void imageResize_thumbnail_테스트() throws Exception {
    // given
    MockMultipartFile file = new MockMultipartFile("file", "file.jpeg", "image/jpeg", new byte[]{1});

    given(convertService.uploadResizeImage(any(CreateImageDto.class))).willReturn("thumbnail url");

    // when
    ResultActions result = mockMvc.perform(
      multipart("/api/images/resize")
        .file(file)
        .param("width", "16")
        .param("height", "9")
        .param("type", "thumbnail")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.url").value("thumbnail url"));
  }
}