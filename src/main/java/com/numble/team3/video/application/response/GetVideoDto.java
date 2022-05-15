package com.numble.team3.video.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetVideoDto {
  @Schema(description = "영상 ID")
  private long videoId;

  @Schema(description = "썸네일 경로")
  private String thumbnailPath;

  @Schema(description = "영상 제목")
  private String title;

  @Schema(description = "닉네임")
  private String nickname;

  @Schema(description = "조회수")
  private long view;

  @Schema(description = "좋아요 수")
  private long like;

  @Schema(description = "영상 타입")
  private VideoType videoType;

  @Schema(description = "업로드 날짜")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Seoul")
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
