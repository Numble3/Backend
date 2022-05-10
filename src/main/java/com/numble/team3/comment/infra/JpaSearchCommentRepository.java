package com.numble.team3.comment.infra;

import com.numble.team3.comment.domain.Comment;
import com.numble.team3.comment.domain.CommentSortCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface JpaSearchCommentRepository {
  Slice<Comment> findAllByVideoIdWithCondition(
      Long videoId, CommentSortCondition commentSortCondition, Pageable pageable);
}
