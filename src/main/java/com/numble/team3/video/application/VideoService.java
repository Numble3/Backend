package com.numble.team3.video.application;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.video.application.request.CreateOrUpdateVideoDto;
import com.numble.team3.video.application.response.GetVideoDetailDto;
import com.numble.team3.video.application.response.GetVideoListDto;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.infra.JpaVideoRepository;
import com.numble.team3.video.infra.VideoRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {
  private final JpaAccountRepository accountRepository;
  private final JpaVideoRepository videoRepository;
  private final VideoRedisUtils videoRedisUtils;

  private Account findByAccountId(Long accountId) {
    return accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
  }

  private Video findByAccountIdAndId(UserInfo userInfo, Long videoId) {
    return videoRepository
        .findByAccountIdAndId(userInfo.getAccountId(), videoId)
        .orElseThrow(VideoNotFoundException::new);
  }

  @Transactional
  public void createVideo(UserInfo userInfo, CreateOrUpdateVideoDto dto) {
    Account account = findByAccountId(userInfo.getAccountId());
    Video video =
        Video.builder()
            .account(account)
            .title(dto.getTitle())
            .content(dto.getContent())
            .videoDuration(dto.getVideoDuration())
            .videoUrl(dto.getVideoUrl())
            .embeddedUrl(dto.getEmbeddedUrl())
            .thumbnailUrl(dto.getThumbnailUrl())
            .category(dto.getCategory())
            .type(dto.getType())
            .build();
    videoRepository.save(video);
  }

  @Transactional
  public void modifyVideo(UserInfo userInfo, Long videoId, CreateOrUpdateVideoDto dto) {
    Video video = findByAccountIdAndId(userInfo, videoId);
    video.changeVideo(
        dto.getTitle(), dto.getContent(), dto.getThumbnailUrl(), dto.getCategory(), dto.getType());
  }

  @Transactional
  public void deleteVideo(UserInfo userInfo, Long videoId) {
    Video video =
        videoRepository
            .findByAccountIdAndId(userInfo.getAccountId(), videoId)
            .orElseThrow(VideoNotFoundException::new);
    video.deleteVideo();
  }

  @Transactional(readOnly = true)
  public GetVideoListDto getAllVideo(PageRequest pageRequest) {
    return GetVideoListDto.fromEntities(videoRepository.findAllWithAccount(pageRequest));
  }

  @Transactional(readOnly = true)
  public GetVideoDetailDto getVideoById(Long videoId) {
    Long viewCount = videoRedisUtils.getViewCountByVideoId(videoId);
    videoRedisUtils.countView(viewCount + 1, videoId);
    return GetVideoDetailDto.fromEntity(
        videoRepository.findById(videoId).orElseThrow(VideoNotFoundException::new));
  }
}
