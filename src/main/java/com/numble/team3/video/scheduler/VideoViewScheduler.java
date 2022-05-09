package com.numble.team3.video.scheduler;

import com.numble.team3.video.application.VideoService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VideoViewScheduler {
  private final VideoService videoService;

  @Scheduled(cron = "0 0 2 * * *")
  public void updateVideoViewCount(){
    log.info("{}: [update view count]", LocalDateTime.now());
    videoService.updateViewCountWithRedis();
  }
}
