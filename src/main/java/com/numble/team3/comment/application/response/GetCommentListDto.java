package com.numble.team3.comment.application.response;

import com.numble.team3.comment.domain.Comment;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@NoArgsConstructor
public class GetCommentListDto {
  private List<GetCommentDto> contents;
  private List<Long> likeComments;
  private boolean hasNext;

  @Builder
  private GetCommentListDto(
      List<GetCommentDto> contents, List<Long> likeComments, boolean hasNext) {
    this.contents = contents;
    this.likeComments = likeComments;
    this.hasNext = hasNext;
  }

  public static GetCommentListDto fromEntities(Slice<Comment> comments) {
    return GetCommentListDto.builder()
      .contents(comments.stream().map(GetCommentDto::fromEntity).collect(Collectors.toList()))
      .hasNext(comments.hasNext())
      .build();
  }

  public static GetCommentListDto fromEntities(Slice<Comment> comments, List<Long> likeComments) {
    return GetCommentListDto.builder()
        .contents(comments.stream().map(GetCommentDto::fromEntity).collect(Collectors.toList()))
        .likeComments(likeComments)
        .hasNext(comments.hasNext())
        .build();
  }
}
