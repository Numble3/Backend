package com.numble.team3.video.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.numble.team3.video.domain.Video;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetVideoDto {
  private long videoId;
  private String thumbnailPath;
  private String title;
  private String nickname;
  private long view;
  private long like;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh-MM-ss", timezone = "Asia/Seoul")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

  @Builder
  public GetVideoDto(
    long videoId,
    String thumbnailPath,
    String title,
    String nickname,
    long view,
    long like,
    LocalDateTime createdAt) {
    this.videoId = videoId;
    this.thumbnailPath = thumbnailPath;
    this.title = title;
    this.nickname = nickname;
    this.view = view;
    this.like = like;
    this.createdAt = createdAt;
  }

  public static GetVideoDto fromEntity(Video video) {
    return GetVideoDto.builder()
      .videoId(video.getId())
      .thumbnailPath(video.getThumbnailUrl())
      .title(video.getTitle())
      .nickname(video.getAccount().getNickname())
      .view(video.getView())
      .like(video.getLike())
      .createdAt(video.getCreatedAt())
      .build();
  }
}
