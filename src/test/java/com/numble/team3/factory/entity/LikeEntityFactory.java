package com.numble.team3.factory.entity;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.numble.team3.like.domain.Like;
import com.numble.team3.video.domain.Video;

public class LikeEntityFactory {

  public static Like createLike() throws Exception {
    Video video = VideoEntityFactory.createVideo();
    Like like = new Like(video, 1L);
    setField(like, "id", 1L);
    return like;
  }
}