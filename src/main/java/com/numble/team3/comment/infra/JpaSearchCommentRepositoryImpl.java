package com.numble.team3.comment.infra;

import static com.numble.team3.comment.domain.QComment.comment;
import static com.numble.team3.account.domain.QAccount.account;
import static com.numble.team3.video.domain.QVideo.video;

import com.numble.team3.comment.domain.Comment;
import com.numble.team3.comment.domain.CommentSortCondition;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JpaSearchCommentRepositoryImpl implements JpaSearchCommentRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public Slice<Comment> findAllByVideoIdWithCondition(
      Long videoId, CommentSortCondition commentSortCondition, Pageable pageable) {
    List<Comment> contents =
        queryFactory
            .selectFrom(comment)
            .innerJoin(comment.account, account)
            .fetchJoin()
            .innerJoin(comment.video, video)
            .fetchJoin()
            .where(comment.video.id.eq(videoId))
            .orderBy(commentSort(commentSortCondition))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

    boolean hasNext = false;
    if (contents.size() > pageable.getPageSize()) {
      contents.remove(pageable.getPageSize());
      hasNext = true;
    }
    return new SliceImpl<>(contents, pageable, hasNext);
  }

  private OrderSpecifier<?> commentSort(CommentSortCondition commentSortCondition) {
    if (commentSortCondition == CommentSortCondition.POPULARITY) {
      return new OrderSpecifier<>(Order.DESC, comment.like);
    } else {
      return new OrderSpecifier<>(Order.DESC, comment.createdAt);
    }
  }
}
