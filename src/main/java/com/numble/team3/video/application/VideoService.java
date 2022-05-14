package com.numble.team3.video.application;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.admin.application.response.GetAccountVideosDto;
import com.numble.team3.admin.application.response.GetSimpleAccountVideoDto;
import com.numble.team3.admin.application.response.GetVideoDetailForAdminDto;
import com.numble.team3.admin.application.response.GetVideoListForAdminDto;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.video.application.request.CreateOrUpdateVideoDto;
import com.numble.team3.video.application.response.GetVideoDetailDto;
import com.numble.team3.video.application.response.GetVideoListDto;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.VideoUtils;
import com.numble.team3.video.infra.JpaVideoRepository;
import com.numble.team3.video.resolver.SearchCondition;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {
  private final JpaAccountRepository accountRepository;
  private final JpaVideoRepository videoRepository;
  private final VideoUtils videoUtils;

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
        dto.getTitle(),
        dto.getContent(),
        dto.getVideoUrl(),
        dto.getThumbnailUrl(),
        dto.getCategory(),
        dto.getType());
  }

  @Transactional
  public void updateViewCountWithRedis() {
    Map<Long, Long> viewCounts = videoUtils.getAllVideoViewCount();
    viewCounts
        .keySet()
        .forEach(videoId -> videoRepository.updateVideoViewCount(viewCounts.get(videoId), videoId));
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
  public GetVideoListDto getAllVideoByCondition(
      SearchCondition condition, PageRequest pageRequest) {
    return GetVideoListDto.fromEntities(
        videoRepository.searchVideoByCondition(condition, pageRequest));
  }

  @Transactional(readOnly = true)
  public GetVideoDetailDto getVideoById(Long videoId) {
    videoUtils.updateViewCount(videoId);
    return GetVideoDetailDto.fromEntity(
        videoRepository.findById(videoId).orElseThrow(VideoNotFoundException::new));
  }

  @Transactional(readOnly = true)
  public GetVideoListForAdminDto getAllVideoForAdmin(PageRequest pageRequest) {
    return GetVideoListForAdminDto.fromEntities(videoRepository.findAll(pageRequest));
  }

  @Transactional(readOnly = true)
  public GetVideoDetailForAdminDto getVideoByIdForAdmin(Long videoId) {
    return GetVideoDetailForAdminDto.fromEntity(
        videoRepository.findById(videoId).orElseThrow(VideoNotFoundException::new));
  }

  @Transactional
  public void deleteVideoByIdForAdmin(Long videoId) {
    Video video = videoRepository.findById(videoId).orElseThrow(VideoNotFoundException::new);
    video.adminDeleteVideo();
  }

  public GetAccountVideosDto getAccountVideosForAdmin(Pageable pageable, Long accountId) {
    Page<Video> pageResult = videoRepository.findAllByAccountIdWithAdmin(accountId, pageable);

    List<GetSimpleAccountVideoDto> collectResult =
      pageResult.stream().map(video -> GetSimpleAccountVideoDto.fromEntity(video))
        .collect(Collectors.toList());

    return GetAccountVideosDto.builder()
      .videos(collectResult)
      .totalCount(pageResult.getTotalElements())
      .nowPage(pageResult.getNumber() + 1)
      .totalPage(pageResult.getTotalPages())
      .size(pageResult.getSize())
      .build();
  }
}
