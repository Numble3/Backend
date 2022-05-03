package com.numble.team3.video.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.numble.team3.video.domain.Video;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetVideoDetailDto {
  private long videoId;
  private String thumbnailPath;
  private String title;
  private String content;
  private String nickname;
  private long view;
  private long like;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

  private String category;

  @Builder
  private GetVideoDetailDto(
      long videoId,
      String thumbnailPath,
      String title,
      String content,
      String nickname,
      long view,
      long like,
      LocalDateTime createdAt,
      String category) {
    this.videoId = videoId;
    this.thumbnailPath = thumbnailPath;
    this.title = title;
    this.content = content;
    this.nickname = nickname;
    this.view = view;
    this.like = like;
    this.createdAt = createdAt;
    this.category = category;
  }

  public static GetVideoDetailDto fromEntity(Video video) {
    return GetVideoDetailDto.builder()
        .videoId(video.getId())
        .thumbnailPath(video.getThumbnailUrl())
        .title(video.getTitle())
        .content(video.getContent())
        .nickname(video.getAccount().getNickname())
        .view(video.getView())
        .like(video.getLike())
        .createdAt(video.getCreateAt())
        .category(video.getCategory().getName())
        .build();
  }
}
