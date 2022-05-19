package com.numble.team3.like.infra;

import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.like.application.response.GetLikeVideoDto;
import com.numble.team3.like.application.response.GetLikeVideoRankDto;
import java.util.List;

public interface CustomJpaLikeVideoRepository {
  List<GetLikeVideoDto> getLikesByCategory(UserInfo userInfo, String categoryName, Long likeId, int size);

  List<GetLikeVideoRankDto> getRankingByLikes();
}
