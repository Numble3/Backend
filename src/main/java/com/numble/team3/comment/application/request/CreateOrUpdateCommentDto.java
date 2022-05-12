package com.numble.team3.comment.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateCommentDto {
  @Schema(description = "댓글 내용")
  @NotEmpty(message = "댓글 내용은 반드시 있어야 합니다.")
  private String content;
}
