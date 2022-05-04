package com.numble.team3.like.application.response;

import com.numble.team3.video.domain.Video;
import lombok.Getter;

@Getter
public class GetVideoQuerydsl {

  private Video video;
  private long likes;
}
