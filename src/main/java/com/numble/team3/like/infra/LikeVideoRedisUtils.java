package com.numble.team3.like.infra;

import com.numble.team3.like.application.response.GetLikeVideoRankDto;
import com.numble.team3.like.domain.LikeVideoUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeVideoRedisUtils implements LikeVideoUtils {

  private final LikeVideoRedisHelper likeVideoRedisHelper;

  @Override
  public void processChangeDayRanking(String standard, List<GetLikeVideoRankDto> ranking) {
    likeVideoRedisHelper.deleteRanking(standard);
    likeVideoRedisHelper.saveRanking(ranking, standard);
  }

  @Override
  public List<GetLikeVideoRankDto> getDayRanking(String standard) {
    return likeVideoRedisHelper.getRanking(standard);
  }
}
