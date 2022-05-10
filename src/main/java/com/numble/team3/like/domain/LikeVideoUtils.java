package com.numble.team3.like.domain;

import com.numble.team3.like.application.response.GetVideoRankDto;
import java.util.List;

public interface LikeVideoUtils {

  void processChangeDayRanking(String standard, List<GetVideoRankDto> ranking);

  List<GetVideoRankDto> getDayRanking(String standard);
}
