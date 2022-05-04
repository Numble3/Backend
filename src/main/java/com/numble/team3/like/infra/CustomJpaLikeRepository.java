package com.numble.team3.like.infra;

import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.like.application.response.GetLikeDto;
import com.numble.team3.like.application.response.GetVideoRankDto;
import java.util.List;

public interface CustomJpaLikeRepository {
  List<GetLikeDto> getLikesByCategory(UserInfo userInfo, String categoryName, Long likeId, int size);

  List<GetVideoRankDto> getRankingByLikes();
}
