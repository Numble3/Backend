package com.numble.team3.like.controller;

import static com.numble.team3.factory.UserInfoFactory.createUserInfo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.resolver.LoginMethodArgumentResolver;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.like.application.LikeVideoService;
import com.numble.team3.like.application.advice.LikeVideoRestControllerAdvice;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("LikeRestControllerAdvice 테스트")
public class LikeVideoVideoControllerAdviceTest {

  @Mock
  LikeVideoService likeVideoService;

  @Mock
  LoginMethodArgumentResolver loginMethodArgumentResolver;

  @InjectMocks
  LikeVideoController likeVideoController;

  MockMvc mockMvc;

  @BeforeEach
  void beforeEach() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(likeVideoController)
      .setCustomArgumentResolvers(loginMethodArgumentResolver)
      .setControllerAdvice(new LikeVideoRestControllerAdvice())
      .addFilter(new CharacterEncodingFilter("UTF-8", true))
      .alwaysDo(print())
      .build();
  }

  @Test
  void addLike_파라미터_누락_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);

    // when
    ResultActions result = mockMvc.perform(
      post("/api/likes/add")
        .header("Authorization", "Bearer access token")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("쿼리 파라미터를 확인해주세요."));
  }

  @Test
  void deleteLike_파라미터_누락_실패_테스트() throws Exception {
    // given

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/likes/delete")
        .header("Authorization", "Bearer access token")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("쿼리 파라미터를 확인해주세요."));
  }

  @Test
  void getLikesByCategory_카테고리_파라미터_누락_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);

    // when
    ResultActions result = mockMvc.perform(
      get("/api/likes/")
        .param("size", "3")
        .header("Authorization", "access token")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("쿼리 파라미터를 확인해주세요."));
  }

  @Test
  void getLikesByCategory_size_파라미터_누락_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);

    // when
    ResultActions result = mockMvc.perform(
      get("/api/likes/")
        .param("category", "cat")
        .header("Authorization", "access token")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("쿼리 파라미터를 확인해주세요."));
  }
}

