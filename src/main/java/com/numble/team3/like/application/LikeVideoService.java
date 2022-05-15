package com.numble.team3.like.application;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.exception.like.LikeVideoNotFoundException;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.like.application.response.GetAllLikeVideoListDto;
import com.numble.team3.like.application.response.GetLikeVideoCategoryListLimitDto;
import com.numble.team3.like.application.response.GetLikeVideoDto;
import com.numble.team3.like.application.response.GetLikeListDto;
import com.numble.team3.like.application.response.GetVideoRankDto;
import com.numble.team3.like.domain.LikeVideo;
import com.numble.team3.like.domain.LikeVideoUtils;
import com.numble.team3.like.infra.JpaLikeVideoRepository;
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
public class LikeVideoService {

  @Value("${like.category.limit}")
  private int limit;

  private final JpaLikeVideoRepository likeRepository;
  private final JpaVideoRepository videoRepository;
  private final LikeVideoUtils likeVideoUtils;

  @Transactional
  public void addLike(UserInfo userInfo, Long videoId, VideoCategory category) {
    likeRepository.save(
      new LikeVideo(
        videoRepository.findById(videoId).orElseThrow(VideoNotFoundException::new),
        userInfo.getAccountId(),
        category));
  }

  @Transactional
  public void deleteLike(UserInfo userInfo, Long videoId) {
    LikeVideo like =
      likeRepository.getLikeByAccountIdAndVideoId(userInfo.getAccountId(), videoId)
        .orElseThrow(LikeVideoNotFoundException::new);

    likeRepository.delete(like);
  }

  @Transactional(readOnly = true)
  public GetLikeListDto getLikesByCategory(
    UserInfo userInfo,
    String categoryName,
    Long likeId,
    int size) {

    List<GetLikeVideoDto> likes = likeRepository.getLikesByCategory(userInfo, categoryName, likeId, size);

    if (likes.size() < size) {
      return new GetLikeListDto(likes, null);
    }
    else {
      return new GetLikeListDto(likes, likes.get(likes.size() - 1).getId());
    }
  }

  @Transactional(readOnly = true)
  public GetAllLikeVideoListDto getLikesHierarchy(UserInfo userInfo) {
    Map<String, List<GetLikeVideoDto>> rankHierarchy = Arrays.stream(VideoCategory.values()).collect(
      groupingBy(value -> value.getName(), mapping(
        value -> likeRepository.getAllLikesWithLimit(userInfo.getAccountId(), value, limit)
          .stream().map(like -> GetLikeVideoDto.fromEntity(like)).collect(toList()),
        Collector.of(ArrayList::new, List::addAll, (likes, getLikeDto) -> {
          likes.addAll(getLikeDto);
          return likes;
        })
      )));

    Map<String, GetLikeVideoCategoryListLimitDto> result = rankHierarchy.entrySet().stream()
      .collect(Collectors.toMap(
        entry -> entry.getKey(),
        entry -> {
          List<GetLikeVideoDto> list = rankHierarchy.get(entry.getKey());
          if (list.size() == 0) {
            return new GetLikeVideoCategoryListLimitDto(list, null);
          } else {
            return new GetLikeVideoCategoryListLimitDto(list, list.get(list.size() - 1).getId());
          }
        }
      ));

    return new GetAllLikeVideoListDto(result);
  }

  @Transactional(readOnly = true)
  public void rankByDayScheduler(String standard) {
    likeVideoUtils.processChangeDayRanking(standard, likeRepository.getRankingByLikes());
  }

  public List<GetVideoRankDto> getRank(String standard) {
    return likeVideoUtils.getDayRanking(standard);
  }

  public List<GetVideoRankDto> getRank(String standard, VideoCategory videoCategory) {
    List<GetVideoRankDto> dtos = likeVideoUtils.getDayRanking(standard);
    return dtos.stream().filter(dto -> dto.getVideoDto().getVideoCategory().equals(videoCategory)).collect(toList());
  }
}
