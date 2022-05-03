package com.numble.team3.video.application;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.common.infra.S3Uploader;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.video.application.request.CreateOrUpdateVideoDto;
import com.numble.team3.video.application.response.GetVideoDetailDto;
import com.numble.team3.video.application.response.GetVideoListDto;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.VideoUtils;
import com.numble.team3.video.infra.JpaVideoRepository;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {
  private final JpaAccountRepository accountRepository;
  private final JpaVideoRepository videoRepository;
  private final VideoUtils videoUtils;
  private final S3Uploader s3Uploader;

  private Account findByAccountId(Long accountId) {
    return accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
  }

  private Video findByAccountIdAndId(UserInfo userInfo, Long videoId){
    return videoRepository.findByAccountIdAndId(userInfo.getAccountId(), videoId).orElseThrow(VideoNotFoundException::new);
  }

  @Transactional
  public void createVideo(UserInfo userInfo, CreateOrUpdateVideoDto dto, MultipartFile videoFile)
      throws IOException {
    String convertedVideoDir = videoUtils.convertVideo(videoFile);
    log.info("converted Video Directory: {}", convertedVideoDir);
    long duration =
        videoUtils.extractVideoDuration(
            convertedVideoDir + File.separator + videoFile.getOriginalFilename());
    String videoAccessPath = s3Uploader.uploadDirectoryWithM3u8(convertedVideoDir);
    Account account = findByAccountId(userInfo.getAccountId());
    Video video =
        Video.builder()
            .account(account)
            .accountId(account.getId())
            .title(dto.getTitle())
            .content(dto.getContent())
            .videoDuration(duration)
            .videoUrl(videoAccessPath)
            .thumbnailUrl(dto.getThumbnailUrl())
            .category(dto.getCategory())
            .build();
    videoRepository.save(video);
  }

  @Transactional
  public void modifyVideo(UserInfo userInfo, Long videoId, CreateOrUpdateVideoDto dto){
    Video video = findByAccountIdAndId(userInfo, videoId);
    video.changeVideo(dto.getTitle(), dto.getContent(), dto.getThumbnailUrl());
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
    return GetVideoDetailDto.fromEntity(
        videoRepository.findById(videoId).orElseThrow(VideoNotFoundException::new));
  }
}
