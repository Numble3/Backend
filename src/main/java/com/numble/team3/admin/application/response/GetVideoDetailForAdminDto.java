package com.numble.team3.admin.application.response;

import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetVideoDetailForAdminDto {
  private String title;
  private String content;
  private String nickname;
  private LocalDateTime createdAt;
  private Long like;
  private Long view;
  private VideoType type;
  private String thumbnailUrl;
  private String videoUrl;

  private GetVideoDetailForAdminDto(
      String title,
      String content,
      String nickname,
      LocalDateTime createdAt,
      Long like,
      Long view,
      VideoType type,
      String thumbnailUrl,
      String videoUrl) {
    this.title = title;
    this.content = content;
    this.nickname = nickname;
    this.createdAt = createdAt;
    this.like = like;
    this.view = view;
    this.type = type;
    this.thumbnailUrl = thumbnailUrl;
    this.videoUrl = videoUrl;
  }

  public static GetVideoDetailForAdminDto fromEntity(Video video) {
    return GetVideoDetailForAdminDto.builder()
        .title(video.getTitle())
        .content(video.getContent())
        .nickname(video.getAccount().getNickname())
        .createdAt(video.getCreatedAt())
        .like(video.getLike())
        .view(video.getView())
        .type(video.getType())
        .thumbnailUrl(video.getThumbnailUrl())
        .videoUrl(video.getVideoUrl())
        .build();
  }
}
