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

  @Schema(description = "관리자 영상 상태")
  private String videoAdminState;

  private GetVideoSimpleForAdminDto(
      String nickname, String title, String thumbnailUrl, Long videoId, String videoAdminState) {
    this.nickname = nickname;
    this.title = title;
    this.thumbnailUrl = thumbnailUrl;
    this.videoId = videoId;
    this.videoAdminState = videoAdminState;
  }

  public static GetVideoSimpleForAdminDto fromEntity(Video video) {
    return GetVideoSimpleForAdminDto.builder()
        .nickname(video.getAccount().getNickname())
        .title(video.getTitle())
        .thumbnailUrl(video.getThumbnailUrl())
        .videoId(video.getId())
        .videoAdminState(video.isAdminDeleteYn() ? "deleted" : null)
        .build();
  }
}
