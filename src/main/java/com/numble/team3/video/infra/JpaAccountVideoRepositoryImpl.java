package com.numble.team3.video.infra;

import static com.numble.team3.account.domain.QAccount.*;
import static com.numble.team3.video.domain.QVideo.video;

import com.numble.team3.video.application.response.GetVideoDto;
import com.numble.team3.video.domain.enums.VideoSortCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JpaAccountVideoRepositoryImpl implements JpaAccountVideoRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<GetVideoDto> getMyVideoDtos(
    VideoSortCondition sort, Long accountId, Long videoId, int limit) {
    return queryFactory
      .select(
        Projections.constructor(GetVideoDto.class,
          video.id, video.thumbnailUrl, video.title,
          video.account.nickname, video.view, video.like,
          video.createdAt, video.type, video.account.profile,
          video.category.stringValue()
        ))
      .from(video)
      .innerJoin(video.account, account)
      .where(
        video.deleteYn.isFalse(),
        video.adminDeleteYn.isFalse(),
        ltVideoId(videoId),
        video.account.id.eq(accountId)
      )
      .orderBy(videoSortList(sort).stream().toArray(OrderSpecifier[]::new))
      .limit(limit)
      .fetch();
  }

  private List<OrderSpecifier> videoSortList(VideoSortCondition sort) {
    List<OrderSpecifier> orders = new ArrayList<>();

    if (sort == VideoSortCondition.POPULARITY) {
      orders.add(video.like.desc());
    }

    orders.add(video.createdAt.desc());
    return orders;
  }

  private BooleanExpression ltVideoId(Long videoId) {
    if (videoId == null) {
      return null;
    }

    return video.id.lt(videoId);
  }
}
