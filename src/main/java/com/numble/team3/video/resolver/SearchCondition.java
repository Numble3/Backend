package com.numble.team3.video.resolver;

import com.numble.team3.video.domain.enums.VideoCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCondition {
  private String title;
  private VideoCategory category;
}
