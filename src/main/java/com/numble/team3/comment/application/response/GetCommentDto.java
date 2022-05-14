package com.numble.team3.comment.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.numble.team3.comment.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCommentDto {
  @Schema(name = "댓글 ID", description = "댓글 ID")
  private Long commentId;

  @Schema(description = "작성자 닉네임")
  private String nickname;

  @Schema(description = "작성자 프로필 사진 경로")
  private String profilePath;

  @Schema(description = "댓글 내용")
  private String content;

  @Schema(description = "종아요 수")
  private long like;

  @Schema(description = "작성 일자")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
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
