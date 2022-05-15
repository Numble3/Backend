package com.numble.team3.video.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Locale;
import lombok.Getter;

@Getter
public enum VideoType {
  @JsonProperty("임베딩 영상")
  EMBEDDED("임베딩 영상"),

  @JsonProperty("직접 업로드")
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
