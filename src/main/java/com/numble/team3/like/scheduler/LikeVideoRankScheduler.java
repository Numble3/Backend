package com.numble.team3.like.scheduler;

import com.numble.team3.like.application.LikeVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeVideoRankScheduler {

  private final LikeVideoService likeVideoService;

  @Scheduled(cron = "0 30 0 * * *")
  protected void rankByDayScheduler() {
    likeVideoService.rankByDayScheduler("day");
  }
}
