package com.numble.team3.comment.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModelProperty;
import java.util.Locale;

public enum CommentSortCondition {
  @ApiModelProperty(name = "인기순")
  POPULARITY,
  @ApiModelProperty(name = "최신순")
  LATEST;

  public static final String DEFAULT = "POPULARITY";

  @JsonCreator
  public static CommentSortCondition from(String s) {
    return CommentSortCondition.valueOf(s.toUpperCase(Locale.ROOT));
  }
}
