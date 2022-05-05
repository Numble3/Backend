package com.numble.team3.like.infra;

import com.numble.team3.like.application.response.GetVideoRankDto;
import com.numble.team3.like.domain.LikeUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeRedisUtils implements LikeUtils {

  private final LikeRedisHelper likeRedisHelper;

  @Override
  public void processChangeDayRanking(String standard, List<GetVideoRankDto> ranking) {
    likeRedisHelper.deleteRanking(standard);
    likeRedisHelper.saveRanking(ranking, standard);
  }

  @Override
  public List<GetVideoRankDto> getDayRanking(String standard) {
    return likeRedisHelper.getRanking(standard);
  }
}
