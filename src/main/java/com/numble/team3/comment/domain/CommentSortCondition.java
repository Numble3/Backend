package com.numble.team3.comment.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Locale;

public enum CommentSortCondition {
  POPULARITY,
  LATEST;

  public static final String DEFAULT = "POPULARITY";

  @JsonCreator
  public static CommentSortCondition from(String s) {
    return CommentSortCondition.valueOf(s.toUpperCase(Locale.ROOT));
  }
}
