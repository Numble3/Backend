package com.numble.team3.video;

import static org.junit.jupiter.api.Assertions.*;

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
  public void 비디오_조회수_증가() {
    video.changeViewCountPlusForDev();
    assertEquals(video.getView(), 1);
  }

  @Test
  public void 비디오_좋아요수_감소() {
    video.changeLikeCountMinusForDev();
    assertEquals(video.getLike(),-1);
  }

  @Test
  public void 비디오_좋아요수_증가() {
    video.changeLikeCountPlusForDev();
    assertEquals(video.getLike(), 1);
  }
}
