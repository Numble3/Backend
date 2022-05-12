package com.numble.team3.comment.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.numble.team3.comment.domain.Comment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCommentDto {
  private Long commentId;
  private String nickname;
  private String profilePath;
  private String content;
  private long like;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd hh-MM-ss",
      timezone = "Asia/Seoul")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

  @Builder
  private GetCommentDto(
      Long commentId,
      String nickname,
      String profilePath,
      String content,
      long like,
      LocalDateTime createdAt) {
    this.commentId = commentId;
    this.nickname = nickname;
    this.profilePath = profilePath;
    this.content = content;
    this.like = like;
    this.createdAt = createdAt;
  }

  public static GetCommentDto fromEntity(Comment comment) {
    return GetCommentDto.builder()
        .commentId(comment.getId())
        .nickname(comment.getAccount().getNickname())
        .profilePath(comment.getAccount().getProfile())
        .content(comment.getContent())
        .like(comment.getLike())
        .createdAt(comment.getCreatedAt())
        .build();
  }
}
