package com.numble.team3.video.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.domain.enums.VideoType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetVideoDetailDto {
  @Schema(description = "영상 ID")
  private long videoId;

  @Schema(description = "유저 프로필 사진 경로")
  private String userProfilePath;

  @Schema(description = "영상 경로")
  private String videoUrl;

  @Schema(description = "썸네일 경로")
  private String thumbnailUrl;

  @Schema(description = "영상 시간")
  private Long videoDuration;

  @Schema(description = "영상 제목")
  private String title;

  @Schema(description = "영상 내용")
  private String content;

  @Schema(description = "닉네임")
  private String nickname;

  @Schema(description = "조회수")
  private long view;

  @Schema(description = "좋아요 수")
  private long like;

  @Schema(description = "유저가 누른 좋아요 여부")
  private Boolean userLikeVideo;

  @Schema(description = "영상 카테고리")
  private VideoCategory category;

  @Schema(description = "영상 종류")
  private VideoType type;

  @Schema(description = "업로드 날짜")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Seoul")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

  @Builder
  private GetVideoDetailDto(
      long videoId,
      String videoUrl,
      String userProfilePath,
      String title,
      String content,
      String nickname,
      long view,
      long like,
      LocalDateTime createdAt,
      VideoCategory category,
      VideoType type,
      Long videoDuration,
      String thumbnailUrl) {
    this.videoId = videoId;
    this.videoUrl = videoUrl;
    this.userProfilePath = userProfilePath;
    this.title = title;
    this.content = content;
    this.nickname = nickname;
    this.view = view;
    this.like = like;
    this.createdAt = createdAt;
    this.category = category;
    this.type = type;
    this.videoDuration = videoDuration;
    this.thumbnailUrl = thumbnailUrl;
  }

  public static GetVideoDetailDto fromEntity(Video video) {
    return GetVideoDetailDto.builder()
        .videoId(video.getId())
        .videoUrl(video.getVideoUrl())
        .userProfilePath(video.getAccount().getProfile())
        .title(video.getTitle())
        .content(video.getContent())
        .nickname(video.getAccount().getNickname())
        .view(video.getView())
        .like(video.getLike())
        .createdAt(video.getCreatedAt())
        .category(video.getCategory())
        .type(video.getType())
        .videoDuration(video.getVideoDuration())
        .thumbnailUrl(video.getThumbnailUrl())
        .build();
  }

  public GetVideoDetailDto userLikeVideo(boolean isLike) {
    this.userLikeVideo = isLike;
    return this;
  }
}
