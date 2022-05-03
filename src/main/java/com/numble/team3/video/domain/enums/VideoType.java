package com.numble.team3.video.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Locale;
import lombok.Getter;

@Getter
public enum VideoType {
  EMBEDDED("임베딩 영상"),
  VIDEO("직접 업로드");

  private final String type;

  VideoType(String type) {
    this.type = type;
  }

  @JsonCreator
  public static VideoType from(String s) {
    return VideoType.valueOf(s.toUpperCase(Locale.ROOT));
  }
}
