package com.numble.team3.video.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.domain.enums.VideoType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetVideoDetailDto {
  private long videoId;
  private String thumbnailPath;
  private String videoUrl;
  private String title;
  private String content;
  private String nickname;
  private long view;
  private long like;
  private VideoCategory category;
  private VideoType type;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

  @Builder
  private GetVideoDetailDto(
      long videoId,
      String videoUrl,
      String thumbnailPath,
      String title,
      String content,
      String nickname,
      long view,
      long like,
      LocalDateTime createdAt,
      VideoCategory category,
      VideoType type) {
    this.videoId = videoId;
    this.videoUrl = videoUrl;
    this.thumbnailPath = thumbnailPath;
    this.title = title;
    this.content = content;
    this.nickname = nickname;
    this.view = view;
    this.like = like;
    this.createdAt = createdAt;
    this.category = category;
    this.type = type;
  }

  public static GetVideoDetailDto fromEntity(Video video) {
    return GetVideoDetailDto.builder()
        .videoId(video.getId())
        .videoUrl(video.getVideoUrlByVideoType())
        .thumbnailPath(video.getThumbnailUrl())
        .title(video.getTitle())
        .content(video.getContent())
        .nickname(video.getAccount().getNickname())
        .view(video.getView())
        .like(video.getLike())
        .createdAt(video.getCreatedAt())
        .category(video.getCategory())
        .type(video.getType())
        .build();
  }
}
