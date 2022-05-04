package com.numble.team3.like.infra;

import static com.numble.team3.account.domain.QAccount.account;
import static com.numble.team3.like.domain.QLike.like;

import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.like.application.response.GetLikeDto;
import com.numble.team3.like.application.response.GetLikeRankVideoDto;
import com.numble.team3.like.application.response.GetVideoQuerydsl;
import com.numble.team3.like.application.response.GetVideoRankDto;
import com.numble.team3.like.domain.Like;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomJpaLikeRepositoryImpl implements CustomJpaLikeRepository {

  @Value("${like.category.rank.limit}")
  private int limit;

  private final JPAQueryFactory queryFactory;

  @Override
  public List<GetLikeDto> getLikesByCategory(
    UserInfo userInfo,
    String categoryName,
    Long likeId,
    int size) {

    List<Like> likes = queryFactory.select(like)
      .from(like)
      .innerJoin(like.video.account, account)
      .where(
        like.accountId.eq(userInfo.getAccountId()),
        ltLikeId(likeId),
        like.video.category.eq(VideoCategory.from(categoryName))
      )
      .orderBy(like.id.desc())
      .limit(size)
      .fetch();

    return likes.stream().map(l -> GetLikeDto.fromEntity(l)).collect(Collectors.toList());
  }

  @Override
  public List<GetVideoRankDto> getRankingByLikes() {
    LocalDate today = LocalDate.now();
    LocalDate yesterday = today.minusDays(1L);

    List<GetVideoQuerydsl> rank = queryFactory.select(
        Projections.fields(GetVideoQuerydsl.class,
          like.video.as("video"),
          like.id.count().as("likes")
        ))
      .from(like)
      .innerJoin(like.video.account, account).fetchJoin()
      .where(
        like.createdAt.goe(yesterday.atStartOfDay()),
        like.createdAt.lt(today.atStartOfDay())
      )
      .groupBy(like.video.id)
      .having(whereVideoCategory(null))
      .orderBy(like.id.count().desc(), like.video.id.desc())
      .limit(limit)
      .fetch();

    return rank.stream().map(
      getVideoQuerydsl -> new GetVideoRankDto(GetLikeRankVideoDto.fromEntity(getVideoQuerydsl.getVideo()),
        getVideoQuerydsl.getLikes())).collect(
      Collectors.toList());
  }

  private BooleanExpression whereVideoCategory(VideoCategory videoCategory) {
    if (videoCategory == null) {
      return null;
    }

    return like.video.category.eq(videoCategory);
  }

  private BooleanExpression ltLikeId(Long likeId) {
    if (likeId == null) {
      return null;
    }

    return like.id.lt(likeId);
  }
}