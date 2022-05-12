package com.numble.team3.video.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.video.annotation.CreateVideoSwagger;
import com.numble.team3.video.annotation.DeleteVideoSwagger;
import com.numble.team3.video.annotation.GetVideoAllSwagger;
import com.numble.team3.video.annotation.GetVideoDetailSwagger;
import com.numble.team3.video.annotation.PutVideoSwagger;
import com.numble.team3.video.annotation.SearchFilter;
import com.numble.team3.video.application.VideoService;
import com.numble.team3.video.application.request.CreateOrUpdateVideoDto;
import com.numble.team3.video.resolver.SearchCondition;
import com.numble.team3.video.application.response.GetVideoDetailDto;
import com.numble.team3.video.application.response.GetVideoListDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "영상 생성, 수정, 삭제, 조회 api")
public class VideoController {

  private final VideoService videoService;

  @CreateVideoSwagger
  @PostMapping(value = "/videos")
  public ResponseEntity createVideo(
      @ApiParam(hidden = true) @LoginUser UserInfo userInfo,
      @Valid @RequestBody CreateOrUpdateVideoDto dto) {
    videoService.createVideo(userInfo, dto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetVideoAllSwagger
  @GetMapping("/videos")
  public ResponseEntity<GetVideoListDto> getAllVideo(
      @ApiParam(value = "페이지 번호", defaultValue = "0", required = true)
          @RequestParam(defaultValue = "0")
          int page,
      @ApiParam(value = "페이지 크기", defaultValue = "5", required = true)
          @RequestParam(defaultValue = "5")
          int size,
      @SearchFilter SearchCondition filter) {
    return ResponseEntity.ok(
        videoService.getAllVideoByCondition(filter, PageRequest.of(page, size)));
  }

  @GetVideoDetailSwagger
  @GetMapping("/videos/{videoId}")
  public ResponseEntity<GetVideoDetailDto> getVideoById(
      @ApiParam(value = "영상 ID", required = true) @PathVariable Long videoId) {
    return ResponseEntity.ok(videoService.getVideoById(videoId));
  }

  @DeleteVideoSwagger
  @DeleteMapping("/videos/{videoId}")
  public ResponseEntity deleteVideoById(
      @LoginUser @ApiParam(hidden = true) UserInfo userInfo,
      @ApiParam(value = "영상 ID", required = true) @PathVariable Long videoId) {
    videoService.deleteVideo(userInfo, videoId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutVideoSwagger
  @PutMapping("/videos/{videoId}")
  public ResponseEntity modifyVideoById(
      @ApiParam(hidden = true) @LoginUser UserInfo userInfo,
      @ApiParam(value = "영상 ID", required = true) @PathVariable Long videoId,
      @Valid @RequestBody CreateOrUpdateVideoDto dto) {
    videoService.modifyVideo(userInfo, videoId, dto);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
