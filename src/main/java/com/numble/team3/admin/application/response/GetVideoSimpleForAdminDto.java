package com.numble.team3.admin.application.response;

import com.numble.team3.video.domain.Video;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetVideoSimpleForAdminDto {
  @Schema(description = "닉네임")
  private String nickname;

  @Schema(description = "영상 제목")
  private String title;

  @Schema(description = "썸네일 경로")
  private String thumbnailUrl;

  @Schema(description = "영상 번호")
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
