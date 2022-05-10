package com.numble.team3.like.application;

import static com.numble.team3.factory.UserInfoFactory.createUserInfo;
import static com.numble.team3.factory.dto.LikeVideoDtoFactory.createGetLikeDto;
import static com.numble.team3.factory.entity.LikeVideoEntityFactory.createLike;
import static com.numble.team3.factory.entity.VideoEntityFactory.createVideo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.exception.like.LikeVideoNotFoundException;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.like.application.response.GetAllLikeVideoListDto;
import com.numble.team3.like.application.response.GetLikeVideoDto;
import com.numble.team3.like.application.response.GetLikeListDto;
import com.numble.team3.like.application.response.GetVideoRankDto;
import com.numble.team3.like.domain.LikeVideo;
import com.numble.team3.like.domain.LikeVideoUtils;
import com.numble.team3.like.infra.JpaLikeVideoRepository;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.infra.JpaVideoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("LikeService 테스트")
class LikeVideoServiceTest {

  @Mock
  JpaLikeVideoRepository likeRepository;

  @Mock
  JpaVideoRepository videoRepository;

  @Mock
  LikeVideoUtils likeVideoUtils;

  LikeVideoService likeVideoService;

  @BeforeEach
  void beforeEach() {
    likeVideoService = new LikeVideoService(likeRepository, videoRepository, likeVideoUtils);
  }

  @Test
  void addLike_성공_테스트() throws Exception {
    // given
    Video video = createVideo();
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);

    given(videoRepository.findById(anyLong())).willReturn(Optional.ofNullable(video));
    given(likeRepository.save(any(LikeVideo.class))).willReturn(null);

    // when
    likeVideoService.addLike(userInfo, 1L);

    // then
    verify(likeRepository).save(any(LikeVideo.class));
  }

  @Test
  void addLike_없는_비디오_id_실패_테스트() {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);

    given(videoRepository.findById(anyLong())).willReturn(Optional.empty());

    // when, then
    assertThrows(VideoNotFoundException.class, () -> likeVideoService.addLike(userInfo, 1L));
  }

  @Test
  void deleteLike_성공_테스트() {
    // given
    given(likeRepository.existsById(anyLong())).willReturn(true);

    // when
    likeVideoService.deleteLike(1L);

    // then
    verify(likeRepository).deleteById(anyLong());
  }

  @Test
  void deleteLike_없는_좋아요_id_실패_테스트() {
    // given
    given(likeRepository.existsById(anyLong())).willThrow(new LikeVideoNotFoundException());

    // when, then
    assertThrows(LikeVideoNotFoundException.class, () -> likeVideoService.deleteLike(1L));
  }

  @Test
  void getLikesByCategory_리스트_없는_성공_테스트() {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);

    given(likeRepository.getLikesByCategory(any(UserInfo.class), anyString(), anyLong(), anyInt()))
      .willReturn(List.of());

    // when
    GetLikeListDto result = likeVideoService.getLikesByCategory(userInfo, "카테고리 이름", 1L, 1);

    // then
    assertEquals(0, result.getLikes().size());
    assertEquals(null, result.getLastLikeId());
  }

  @Test
  void getLikesByCategory_리스트_있는_테스트() throws Exception {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);
    GetLikeVideoDto dto = createGetLikeDto();

    given(likeRepository.getLikesByCategory(any(UserInfo.class), anyString(), anyLong(), anyInt()))
      .willReturn(List.of(dto));

    // when
    GetLikeListDto result = likeVideoService.getLikesByCategory(userInfo, "카테고리 이름", 1L, 1);

    // then
    assertEquals(1, result.getLikes().size());
    assertEquals(1L, result.getLastLikeId());
  }

  @Test
  void getLikesHierarchy_성공_테스트() throws Exception {
    // given
    UserInfo userInfo = createUserInfo(1L, RoleType.ROLE_USER);
    LikeVideo likeVideo = createLike();

    given(likeRepository.getAllLikesWithLimit(anyLong(), eq(VideoCategory.CAT), anyInt()))
      .willReturn(List.of(likeVideo));

    // when
    GetAllLikeVideoListDto result = likeVideoService.getLikesHierarchy(userInfo);

    // then
    assertEquals(1, result.getLikes().get(VideoCategory.CAT.getName()).getGetLikeVideoDtos().size());
    assertEquals(1L, result.getLikes().get(VideoCategory.CAT.getName()).getLastLikeId());
    assertEquals(0, result.getLikes().get(VideoCategory.DOG.getName()).getGetLikeVideoDtos().size());
  }

  @Test
  void rankByDayScheduler_성공_테스트() {
    // given
    willDoNothing().given(likeVideoUtils).processChangeDayRanking(anyString(), any(List.class));

    // when
    likeVideoService.rankByDayScheduler("day");

    // then
    verify(likeVideoUtils).processChangeDayRanking(anyString(), any(List.class));
  }

  @Test
  void getRank_성공_테스트() {
    // given
    given(likeVideoUtils.getDayRanking(anyString())).willReturn(List.of());

    // when
    List<GetVideoRankDto> result = likeVideoService.getRank("day");

    // then
    assertEquals(0, result.size());
  }
}