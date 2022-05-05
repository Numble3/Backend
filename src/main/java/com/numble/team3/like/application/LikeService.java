package com.numble.team3.like.application;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.exception.like.LikeNotFoundException;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.like.application.response.GetAllLikeListDto;
import com.numble.team3.like.application.response.GetLikeCategoryListLimitDto;
import com.numble.team3.like.application.response.GetLikeDto;
import com.numble.team3.like.application.response.GetLikeListDto;
import com.numble.team3.like.application.response.GetVideoRankDto;
import com.numble.team3.like.domain.Like;
import com.numble.team3.like.domain.LikeUtils;
import com.numble.team3.like.infra.JpaLikeRepository;
import com.numble.team3.like.infra.LikeRedisHelper;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.infra.JpaVideoRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

  @Value("${like.category.limit}")
  private int limit;

  private final JpaLikeRepository likeRepository;
  private final JpaVideoRepository videoRepository;
  private final LikeUtils likeUtils;

  @Transactional
  public void addLike(UserInfo userInfo, Long videoId) {
    likeRepository.save(
      new Like(videoRepository.findById(videoId).orElseThrow(VideoNotFoundException::new),
        userInfo.getAccountId()));
  }

  @Transactional
  public void deleteLike(Long likeId) {
    if (!likeRepository.existsById(likeId)) {
      throw new LikeNotFoundException();
    }

    likeRepository.deleteById(likeId);
  }

  @Transactional(readOnly = true)
  public GetLikeListDto getLikesByCategory(
    UserInfo userInfo,
    String categoryName,
    Long likeId,
    int size) {

    List<GetLikeDto> likes = likeRepository.getLikesByCategory(userInfo, categoryName, likeId, size);

    if (likes.size() == 0) {
      return new GetLikeListDto(likes, null);
    }
    else {
      return new GetLikeListDto(likes, likes.get(likes.size() - 1).getId());
    }
  }

  @Transactional(readOnly = true)
  public GetAllLikeListDto getLikesHierarchy(UserInfo userInfo) {
    Map<String, List<GetLikeDto>> rankHierarchy = Arrays.stream(VideoCategory.values()).collect(
      groupingBy(value -> value.getName(), mapping(
        value -> likeRepository.getAllLikesWithLimit(userInfo.getAccountId(), value, limit)
          .stream().map(like -> GetLikeDto.fromEntity(like)).collect(toList()),
        Collector.of(ArrayList::new, List::addAll, (likes, getLikeDto) -> {
          likes.addAll(getLikeDto);
          return likes;
        })
      )));

    Map<String, GetLikeCategoryListLimitDto> result = rankHierarchy.entrySet().stream()
      .collect(Collectors.toMap(
        entry -> entry.getKey(),
        entry -> {
          List<GetLikeDto> list = rankHierarchy.get(entry.getKey());
          if (list.size() == 0) {
            return new GetLikeCategoryListLimitDto(list, null);
          } else {
            return new GetLikeCategoryListLimitDto(list, list.get(list.size() - 1).getId());
          }
        }
      ));

    return new GetAllLikeListDto(result);
  }

  @Transactional(readOnly = true)
  public void rankByDayScheduler(String standard) {
    likeUtils.processChangeDayRanking(standard, likeRepository.getRankingByLikes());
  }

  public List<GetVideoRankDto> getRank(String standard) {
    return likeUtils.getDayRanking(standard);
  }
}
