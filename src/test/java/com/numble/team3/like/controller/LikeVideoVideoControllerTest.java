package com.numble.team3.like.controller;

import static com.numble.team3.factory.UserInfoFactory.*;
import static com.numble.team3.factory.dto.LikeVideoDtoFactory.createGetAllLikeListDto;
import static com.numble.team3.factory.dto.LikeVideoDtoFactory.createGetLikeListDto;
import static com.numble.team3.factory.dto.LikeVideoDtoFactory.createGetVideoRankDtoList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.resolver.LoginMethodArgumentResolver;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.factory.UserInfoFactory;
import com.numble.team3.like.application.LikeVideoService;
import com.numble.team3.like.application.response.GetAllLikeVideoListDto;
import com.numble.team3.like.application.response.GetLikeListDto;
import com.numble.team3.like.application.response.GetVideoRankDto;
import com.numble.team3.video.domain.enums.VideoCategory;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("LikeController 테스트")
class LikeVideoVideoControllerTest {

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
      .alwaysDo(print())
      .build();
  }

  @Test
  void addLike_테스트() throws Exception {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    willDoNothing().given(likeVideoService)
      .addLike(any(UserInfo.class), anyLong(), any(VideoCategory.class));

    // when
    ResultActions result = mockMvc.perform(
      post("/api/likes/add").param("id", "1").param("category", "cat")
        .header("Authorization", "Bearer access token")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isCreated());

    verify(likeVideoService).addLike(any(UserInfo.class), anyLong(), any(VideoCategory.class));
  }

  @Test
  void deleteLike_테스트() throws Exception {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);

    willDoNothing().given(likeVideoService).deleteLike(any(UserInfo.class), anyLong());

    // when
    ResultActions result = mockMvc.perform(
      delete("/api/likes/delete").param("id", "1")
        .header("Authorization", "access token")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isOk());

    verify(likeVideoService).deleteLike(any(UserInfo.class), anyLong());
  }

  @Test
  void getLikesHierarchy_테스트() throws Exception {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);

    GetAllLikeVideoListDto dto = createGetAllLikeListDto();

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    given(likeVideoService.getLikesHierarchy(any(UserInfo.class))).willReturn(dto);

    // when
    ResultActions result = mockMvc.perform(
      get("/api/likes/all")
        .header("Authorization", "access token")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.likes").exists())
      .andExpect(jsonPath("$.likes.고양이").exists())
      .andExpect(jsonPath("$.likes.고양이.lastLikeId").value(1))
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos").exists())
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0]").exists())
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0].id").value(1))
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0].createdAt").exists())
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0].getVideoDto").exists())
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0].getVideoDto.videoId").value(1))
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0].getVideoDto.thumbnailPath").value("https://thumbnail-url"))
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0].getVideoDto.title").value("title"))
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0].getVideoDto.nickname").value("nickname"))
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0].getVideoDto.view").value(1))
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0].getVideoDto.like").value(1))
      .andExpect(jsonPath("$.likes.고양이.getLikeVideoDtos[0].getVideoDto.createdAt").exists());

    verify(likeVideoService).getLikesHierarchy(any(UserInfo.class));
  }

  @Test
  void getLikesByCategory_테스트() throws Exception {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);

    GetLikeListDto dto = createGetLikeListDto();

    given(loginMethodArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginMethodArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
    given(likeVideoService.getLikesByCategory(any(UserInfo.class), anyString(), any(), anyInt()))
      .willReturn(dto);

    // when
    ResultActions result = mockMvc.perform(
      get("/api/likes/")
        .param("category", "cat")
        .param("size", "3")
        .header("Authorization", "access token")
        .accept(MediaType.APPLICATION_JSON_VALUE));

    // then
    result
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.lastLikeId").value(1))
      .andExpect(jsonPath("$.likes").exists())
      .andExpect(jsonPath("$.likes[0].id").value(1))
      .andExpect(jsonPath("$.likes[0].createdAt").exists())
      .andExpect(jsonPath("$.likes[0].getVideoDto").exists())
      .andExpect(jsonPath("$.likes[0].getVideoDto.videoId").value(1))
      .andExpect(jsonPath("$.likes[0].getVideoDto.thumbnailPath").value("https://thumbnail-url"))
      .andExpect(jsonPath("$.likes[0].getVideoDto.title").value("title"))
      .andExpect(jsonPath("$.likes[0].getVideoDto.nickname").value("nickname"))
      .andExpect(jsonPath("$.likes[0].getVideoDto.view").value(1))
      .andExpect(jsonPath("$.likes[0].getVideoDto.like").value(1))
      .andExpect(jsonPath("$.likes[0].getVideoDto.createdAt").exists());

    verify(likeVideoService).getLikesByCategory(any(UserInfo.class), anyString(), any(), anyInt());
  }

  @Test
  void getRankByDay_테스트() throws Exception {
    // given
    List<GetVideoRankDto> dto = createGetVideoRankDtoList();

    given(likeVideoService.getRank(anyString())).willReturn(dto);

    // when
    ResultActions result = mockMvc.perform(
      get("/api/likes/rank/day"));

    // then
    result
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.ranking").exists())
      .andExpect(jsonPath("$.ranking[0].videoDto").exists())
      .andExpect(jsonPath("$.ranking[0].likes").value(50))
      .andExpect(jsonPath("$.ranking[0].videoDto.videoId").value(1))
      .andExpect(jsonPath("$.ranking[0].videoDto.thumbnailPath").value("https://thumbnail-url"))
      .andExpect(jsonPath("$.ranking[0].videoDto.title").value("title"))
      .andExpect(jsonPath("$.ranking[0].videoDto.nickname").value("nickname"))
      .andExpect(jsonPath("$.ranking[0].videoDto.view").value(300))
      .andExpect(jsonPath("$.ranking[0].videoDto.like").value(150))
      .andExpect(jsonPath("$.ranking[0].videoDto.createdAt").exists());
  }
}