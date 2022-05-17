package com.numble.team3.like.infra;

import static com.numble.team3.account.domain.QAccount.account;
import static com.numble.team3.like.domain.QLikeVideo.likeVideo;

import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.like.application.response.GetLikeVideoDto;
import com.numble.team3.like.application.response.GetLikeVideoRankDto;
import com.numble.team3.like.application.response.GetLikeVideoQuerydsl;
import com.numble.team3.like.application.response.GetVideoRankDto;
import com.numble.team3.like.domain.LikeVideo;
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
public class CustomJpaLikeVideoRepositoryImpl implements CustomJpaLikeVideoRepository {

  @Value("${like.category.rank.limit}")
  private int limit;

  private final JPAQueryFactory queryFactory;

  @Override
  public List<GetLikeVideoDto> getLikesByCategory(
    UserInfo userInfo,
    String categoryName,
    Long likeId,
    int size) {

    List<LikeVideo> likeVideos = queryFactory.select(likeVideo)
      .from(likeVideo)
      .innerJoin(likeVideo.video.account, account)
      .where(
        likeVideo.accountId.eq(userInfo.getAccountId()),
        ltLikeId(likeId),
        likeVideo.category.eq(VideoCategory.from(categoryName))
      )
      .orderBy(likeVideo.id.desc())
      .limit(size)
      .fetch();

    return likeVideos.stream().map(l -> GetLikeVideoDto.fromEntity(l)).collect(Collectors.toList());
  }

  @Override
  public List<GetVideoRankDto> getRankingByLikes() {
    LocalDate today = LocalDate.now();
    LocalDate yesterday = today.minusDays(1L);

    List<GetLikeVideoQuerydsl> rank = queryFactory.select(
        Projections.fields(GetLikeVideoQuerydsl.class,
          likeVideo.video.as("video"),
          likeVideo.id.count().as("likes")
        ))
      .from(likeVideo)
      .innerJoin(likeVideo.video.account, account).fetchJoin()
      .where(
        likeVideo.createdAt.goe(yesterday.atStartOfDay()),
        likeVideo.createdAt.lt(today.atStartOfDay())
      )
      .groupBy(likeVideo.video.id)
      .orderBy(likeVideo.id.count().desc(), likeVideo.video.id.desc())
      .limit(limit)
      .fetch();

    return rank.stream().map(
      getLikeVideoQuerydsl -> new GetVideoRankDto(GetLikeVideoRankDto.fromEntity(
        getLikeVideoQuerydsl.getVideo()),
        getLikeVideoQuerydsl.getLikes())).collect(
      Collectors.toList());
  }

  private BooleanExpression ltLikeId(Long likeId) {
    if (likeId == null || likeId == 0) {
      return null;
    }

    return likeVideo.id.lt(likeId);
  }
}