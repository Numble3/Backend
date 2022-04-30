package com.numble.team3.video.application;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.video.application.request.CreateVideoDto;
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

  private Account findByAccountId(Long accountId) {
    return accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
  }

  @Transactional
  public void createVideo(
      UserInfo userInfo, CreateVideoDto dto, MultipartFile thumbnailFile, MultipartFile videoFile)
      throws IOException {
    String convertedVideoDir = videoUtils.convertVideo(videoFile);
    log.info("converted Video Directory: {}", convertedVideoDir);
    //todo 썸네일 업로드 로직 추가
    Account account = findByAccountId(userInfo.getAccountId());
    Video video =
        Video.builder()
            .account(account)
            .accountId(account.getId())
            .title(dto.getTitle())
            .content(dto.getContent())
            .videoDuration(
                videoUtils.extractVideoDuration(
                    convertedVideoDir
                        + File.separator
                        + videoFile.getOriginalFilename()))
            .videoUrl("")
            .thumbnailUrl("")
            .category(dto.getCategory())
            .build();
    videoRepository.save(video);
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
