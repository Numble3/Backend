package com.numble.team3.video.resolver;

import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.domain.enums.VideoSortCondition;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCondition {
  @ApiModelProperty(value = "검색 제목 (생략 가능)")
  private String title;

  @ApiModelProperty(value = "검색 카테고리 (생략 가능)")
  private VideoCategory category;

  @ApiModelProperty(value = "정렬 조건 (생략 가능)")
  private VideoSortCondition sort;
}
