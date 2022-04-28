package com.numble.team3.video.application.request;

import com.numble.team3.video.domain.enums.VideoCategory;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateVideoDto {
  @NotEmpty(message = "영상 제목은 반드시 있어야 합니다.")
  private String title;

  @NotEmpty(message = "영상 내용은 반드시 있어야 합니다.")
  private String content;

  private VideoCategory category;
}
