package com.numble.team3.like.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.numble.team3.like.domain.LikeVideo;
import com.numble.team3.video.application.response.GetVideoDto;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetLikeVideoDto {

  private Long id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

  private GetVideoDto getVideoDto;

  public static GetLikeVideoDto fromEntity(LikeVideo likeVideo) {
    return GetLikeVideoDto.builder()
      .id(likeVideo.getId())
      .createdAt(likeVideo.getCreatedAt())
      .getVideoDto(GetVideoDto.fromEntity(likeVideo.getVideo()))
      .build();
  }
}