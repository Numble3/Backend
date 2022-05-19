package com.numble.team3.like.domain;

import com.numble.team3.like.application.response.GetLikeVideoRankDto;
import java.util.List;

public interface LikeVideoUtils {

  void processChangeDayRanking(String standard, List<GetLikeVideoRankDto> ranking);

  List<GetLikeVideoRankDto> getDayRanking(String standard);
}
