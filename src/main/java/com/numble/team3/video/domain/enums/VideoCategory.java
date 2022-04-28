package com.numble.team3.video.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Locale;
import lombok.Getter;

@Getter
public enum VideoCategory {
  CAT("고양이"),
  DOG("강아지");

  private String name;

  VideoCategory(String name) {
    this.name = name;
  }

  @JsonCreator
  public static VideoCategory from(String s) {
    return VideoCategory.valueOf(s.toUpperCase(Locale.ROOT));
  }
}
