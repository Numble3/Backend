package com.numble.team3.comment.application.response;

import com.numble.team3.comment.domain.Comment;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCommentListDto {
  private List<GetCommentDto> contents;
  private boolean hasNext;

  @Builder
  private GetCommentListDto(List<GetCommentDto> contents, boolean hasNext) {
    this.contents = contents;
    this.hasNext = hasNext;
  }

  public static GetCommentListDto fromEntities(List<Comment> comments) {
    return GetCommentListDto.builder()
        .contents(comments.stream().map(GetCommentDto::fromEntity).collect(Collectors.toList()))
        .hasNext(comments.size() > 0)
        .build();
  }
}
