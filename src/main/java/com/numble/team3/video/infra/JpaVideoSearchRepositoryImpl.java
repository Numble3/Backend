package com.numble.team3.video.infra;

import static com.numble.team3.video.domain.QVideo.video;
import static com.numble.team3.account.domain.QAccount.account;

import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoSortCondition;
import com.numble.team3.video.resolver.SearchCondition;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JpaVideoSearchRepositoryImpl implements JpaVideoSearchRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public Slice<Video> searchVideoByCondition(SearchCondition filter, Pageable pageable) {
    List<Video> contents =
        queryFactory
            .selectFrom(video)
            .leftJoin(video.account, account)
            .fetchJoin()
            .where(
                video.deleteYn.isFalse(),
                video.adminDeleteYn.isFalse(),
                isTitleEq(filter),
                isCategoryEq(filter))
            .orderBy(videoSort(filter))
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

  private OrderSpecifier videoSort(SearchCondition filter) {
    if (filter.getSortCondition() == VideoSortCondition.POPULARITY) {
      return new OrderSpecifier<>(Order.DESC, video.like);
    } else {
      return new OrderSpecifier<>(Order.DESC, video.createdAt);
    }
  }

  private BooleanExpression isTitleEq(SearchCondition filter) {
    if (filter == null || filter.getTitle() == null) {
      return null;
    }
    return video.title.eq(filter.getTitle());
  }

  private BooleanExpression isCategoryEq(SearchCondition filter) {
    if (filter == null || filter.getCategory() == null) {
      return null;
    }

    return video.category.eq(filter.getCategory());
  }
}
