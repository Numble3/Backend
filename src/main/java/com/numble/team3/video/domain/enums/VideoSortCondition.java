package com.numble.team3.video.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModelProperty;
import java.util.Locale;

public enum VideoSortCondition {
  @ApiModelProperty(name = "인기순")
  POPULARITY,
  @ApiModelProperty(name = "최신순")
  LATEST;

  public static final String DEFAULT = "POPULARITY";

  @JsonCreator
  public static VideoSortCondition from(String s) {
    return VideoSortCondition.valueOf(s.toUpperCase(Locale.ROOT));
  }
}
