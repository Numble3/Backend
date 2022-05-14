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
  private String thumbnailUrl;
  private String title;
  private String videoAdminState;

  public static GetSimpleAccountVideoDto fromEntity(Video video) {
    return GetSimpleAccountVideoDto.builder()
        .videoId(video.getId())
        .thumbnailUrl(video.getThumbnailUrl())
        .title(video.getTitle())
        .videoAdminState(video.isAdminDeleteYn() ? "deleted" : null)
        .build();
  }
}
