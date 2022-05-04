package com.numble.team3.video.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.video.application.VideoService;
import com.numble.team3.video.application.request.CreateOrUpdateVideoDto;
import com.numble.team3.video.application.response.GetVideoDetailDto;
import com.numble.team3.video.application.response.GetVideoListDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VideoController {

  private final VideoService videoService;

  @PostMapping(value = "/video")
  public ResponseEntity createVideo(
      @LoginUser UserInfo userInfo,
      @Valid @RequestPart(value = "dto") CreateOrUpdateVideoDto dto) {
    videoService.createVideo(userInfo, dto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/videos")
  public ResponseEntity<GetVideoListDto> getAllVideo(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
    return ResponseEntity.ok(videoService.getAllVideo(PageRequest.of(page, size)));
  }

  @GetMapping("/videos/{videoId}")
  public ResponseEntity<GetVideoDetailDto> getVideoById(@PathVariable Long videoId) {
    return ResponseEntity.ok(videoService.getVideoById(videoId));
  }

  @DeleteMapping("/videos/{videoId}")
  public ResponseEntity deleteVideoById(@LoginUser UserInfo userInfo, @PathVariable Long videoId) {
    videoService.deleteVideo(userInfo, videoId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/videos/{videoId}")
  public ResponseEntity modifyVideoById(
      @LoginUser UserInfo userInfo,
      @PathVariable Long videoId,
      @RequestBody CreateOrUpdateVideoDto dto) {
    videoService.modifyVideo(userInfo, videoId, dto);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
