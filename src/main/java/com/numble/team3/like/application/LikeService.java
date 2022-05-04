package com.numble.team3.like.application;

import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.like.application.response.GetAllLikeListDto;
import com.numble.team3.like.application.response.GetLikeListDto;
import com.numble.team3.like.application.response.GetVideoRankDto;
import java.util.List;

public interface LikeService {

  void addLike(UserInfo userInfo, Long videoId);

  void deleteLike(Long likeId);

  GetLikeListDto getLikesByCategory(UserInfo userInfo, String categoryName, Long likeId, int size);

  GetAllLikeListDto getLikesHierarchy(UserInfo userInfo);

  void rankByDayScheduler(String standard);

  List<GetVideoRankDto> getRank(String standard);
}
