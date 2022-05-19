package com.numble.team3.like.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.domain.enums.VideoType;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetLikeVideoRankDto {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate createdAt;
  private Long like;
  private String nickname;
  private String profileUrl;
  private String thumbnailPath;
  private String title;
  private Long videoId;
  private Long view;
  private String category;
  private String videoType;

  public static GetLikeVideoRankDto fromEntity(Video video) {
    return GetLikeVideoRankDto.builder()
      .category(video.getCategory().toString())
      .createdAt(video.getCreatedAt().toLocalDate())
      .like(video.getLike())
      .nickname(video.getAccount().getNickname())
      .profileUrl(video.getAccount().getProfile())
      .thumbnailPath(video.getThumbnailUrl())
      .title(video.getTitle())
      .videoId(video.getId())
      .videoType(video.getType().toString())
      .view(video.getView())
      .build();
  }
}
