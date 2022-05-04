package com.numble.team3.like.scheduler;

import com.numble.team3.like.application.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeRankScheduler {

  private final LikeService likeService;

  @Scheduled(cron = "0 30 0 * * *")
  protected void rankByDayScheduler() {
    likeService.rankByDayScheduler("day");
  }
}
