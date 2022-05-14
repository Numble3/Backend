package com.numble.team3.admin.application.response;

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
@Builder
public class GetVideoDetailForAdminDto {
  @Schema(description = "영상 제목")
  private String title;

  @Schema(description = "영상 내용")
  private String content;

  @Schema(description = "닉네임")
  private String nickname;

  @Schema(description = "업로드 날짜")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Seoul")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

  @Schema(description = "좋아요 수")
  private Long like;

  @Schema(description = "조회수")
  private Long view;

  @Schema(description = "영상 종류")
  private VideoType type;

  @Schema(description = "썸네일 경로")
  private String thumbnailUrl;

  @Schema(description = "영상 경로")
  private String videoUrl;

  @Schema(description = "관리자 영상 관리 상태")
  private String videoAdminState;

  @Schema(description = "영상 업로더 account ID")
  private Long accountId;

  private GetVideoDetailForAdminDto(
      String title,
      String content,
      String nickname,
      LocalDateTime createdAt,
      Long like,
      Long view,
      VideoType type,
      String thumbnailUrl,
      String videoUrl,
      String videoAdminState,
      Long accountId) {
    this.title = title;
    this.content = content;
    this.nickname = nickname;
    this.createdAt = createdAt;
    this.like = like;
    this.view = view;
    this.type = type;
    this.thumbnailUrl = thumbnailUrl;
    this.videoUrl = videoUrl;
    this.videoAdminState = videoAdminState;
    this.accountId = accountId;
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
        .videoAdminState(video.isAdminDeleteYn() ? "deleted" : null)
        .accountId(video.getAccount().getId())
        .build();
  }
}
