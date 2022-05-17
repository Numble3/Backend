package com.numble.team3.video;

import static com.numble.team3.factory.UserInfoFactory.createUserInfo;
import static com.numble.team3.factory.dto.LikeVideoDtoFactory.createGetLikeDto;
import static com.numble.team3.factory.entity.LikeVideoEntityFactory.*;
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

import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VideoEntityTest {
  Video video;

  @BeforeEach
  public void beforeEach() throws Exception {
    video =
        Video.builder()
            .videoUrl(null)
            .videoDuration(10L)
            .title("title")
            .content("content")
            .thumbnailUrl(null)
            .category(VideoCategory.CAT)
            .account(null)
            .build();
  }

  @Test
  public void 비디오_조회수_증가() throws Exception {
    video.changeViewCountPlusForDev();
    assertEquals(video.getView(), 1);
  }

  @Test
  public void 비디오_좋아요수_감소() throws Exception {
    video.changeLikeCountMinusForDev();
    assertEquals(video.getLike(),-1);
  }

  @Test
  public void 비디오_좋아요수_증가() throws Exception {
    video.changeLikeCountPlusForDev();
    assertEquals(video.getLike(), 1);
  }
}
