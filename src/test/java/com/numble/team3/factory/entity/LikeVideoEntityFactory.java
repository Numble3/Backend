package com.numble.team3.factory.entity;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.numble.team3.like.domain.LikeVideo;
import com.numble.team3.video.domain.Video;

public class LikeVideoEntityFactory {

  public static LikeVideo createLike() throws Exception {
    Video video = VideoEntityFactory.createVideo();
    LikeVideo likeVideo = new LikeVideo(video, 1L);
    setField(likeVideo, "id", 1L);
    return likeVideo;
  }
}