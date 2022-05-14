package com.numble.team3.admin.application.response;

import com.numble.team3.video.domain.Video;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetSimpleAccountVideoDto {

  private Long videoId;
  private String thumbnailPath;
  private String title;

  public static GetSimpleAccountVideoDto fromEntity(Video video) {
    return GetSimpleAccountVideoDto.builder()
      .videoId(video.getId())
      .thumbnailPath(video.getThumbnailUrl())
      .title(video.getTitle())
      .build();
  }
}
