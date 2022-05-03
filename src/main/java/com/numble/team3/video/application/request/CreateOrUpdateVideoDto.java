package com.numble.team3.video.application.request;

import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.domain.enums.VideoType;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateOrUpdateVideoDto {
  @NotEmpty(message = "영상 제목은 반드시 있어야 합니다.")
  private String title;

  @NotEmpty(message = "영상 내용은 반드시 있어야 합니다.")
  private String content;

  @NotEmpty(message = "썸네일 경로는 반드시 있어야 합니다.")
  private String thumbnailUrl;

  private String videoUrl;

  private String embeddedUrl;

  private Long videoDuration;

  private VideoCategory category;

  private VideoType type;
}
