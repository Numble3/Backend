package com.numble.team3.video.factory;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.numble.team3.account.domain.Account;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.domain.enums.VideoType;

public class VideoFactory {
  public static Video createVideoForTest(String title, long view, long like, VideoCategory category,
    Account account) {
    Video video =
      Video.builder()
        .category(category)
        .videoDuration(10L)
        .videoUrl("https://video-url")
        .thumbnailUrl("https://thumbnail-url")
        .type(VideoType.VIDEO)
        .title(title)
        .account(account)
        .content("content")
        .build();
    setField(video, "like", like);
    setField(video, "view", view);
    return video;
  }
}
