package com.numble.team3.account.application.response;

import com.numble.team3.video.domain.Video;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetMySimpleVideoDto {

  private Long videoId;
  private String thumbnailPath;
  private String title;
  private String videoType;

  public static GetMySimpleVideoDto fromEntity(Video video) {
    return GetMySimpleVideoDto.builder()
      .videoId(video.getId())
      .thumbnailPath(video.getThumbnailUrl())
      .title(video.getTitle())
      .videoType(video.getType().getType())
      .build();
  }
}