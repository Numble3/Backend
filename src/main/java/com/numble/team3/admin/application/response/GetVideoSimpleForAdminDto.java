package com.numble.team3.admin.application.response;

import com.numble.team3.video.domain.Video;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetVideoSimpleForAdminDto {
  private String nickname;
  private String title;
  private String thumbnailUrl;
  private Long videoId;

  private GetVideoSimpleForAdminDto(
      String nickname, String title, String thumbnailUrl, Long videoId) {
    this.nickname = nickname;
    this.title = title;
    this.thumbnailUrl = thumbnailUrl;
    this.videoId = videoId;
  }

  public static GetVideoSimpleForAdminDto fromEntity(Video video) {
    return GetVideoSimpleForAdminDto.builder()
        .nickname(video.getAccount().getNickname())
        .title(video.getTitle())
        .thumbnailUrl(video.getThumbnailUrl())
        .videoId(video.getId())
        .build();
  }
}
