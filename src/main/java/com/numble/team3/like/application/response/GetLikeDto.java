package com.numble.team3.like.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.numble.team3.like.domain.Like;
import com.numble.team3.video.application.response.GetVideoDto;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetLikeDto {

  private Long id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

  private GetVideoDto getVideoDto;

  public static GetLikeDto fromEntity(Like like) {
    return GetLikeDto.builder()
      .id(like.getId())
      .createdAt(like.getCreatedAt())
      .getVideoDto(GetVideoDto.fromEntity(like.getVideo()))
      .build();
  }
}