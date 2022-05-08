package com.numble.team3.factory.entity;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.numble.team3.account.domain.RoleType;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoCategory;

public class VideoEntityFactory {

  public static Video createVideo() throws Exception {
    Video video = Video.builder()
      .videoDuration(360L)
      .title("title")
      .content("content")
      .videoUrl("https://video-url")
      .thumbnailUrl("https://thumbnail-url")
      .account(AccountEntityFactory.createAccount(
        1L, "test@email.com", "1234", "닉네임", RoleType.ROLE_USER))
      .category(VideoCategory.CAT)
      .build();

    setField(video, "id", 1L);
    setField(video, "view", 1L);
    setField(video, "like", 1L);
    return video;
  }
}