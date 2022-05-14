package com.numble.team3.admin.controller;

import com.numble.team3.admin.annotation.DeleteVideoForAdminSwagger;
import com.numble.team3.admin.annotation.GetAllVideoForAdminSwagger;
import com.numble.team3.admin.annotation.GetDetailVideoForAdminSwagger;
import com.numble.team3.admin.application.response.GetAdminDeleteVideoStateDto;
import com.numble.team3.admin.application.response.GetVideoDetailForAdminDto;
import com.numble.team3.admin.application.response.GetVideoListForAdminDto;
import com.numble.team3.video.application.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Api(tags = "관리자 영상 조회, 삭제 api")
public class AdminVideoController {
  private final VideoService videoService;

  @GetAllVideoForAdminSwagger
  @GetMapping(value = "/videos", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole(ROLE_ADMIN)")
  public ResponseEntity<GetVideoListForAdminDto> getAllVideo(
      @ApiParam(name = "페이지 번호", defaultValue = "1", required = true)
          @RequestParam(value = "page", defaultValue = "1")
          int page,
      @ApiParam(name = "페이지 크기", defaultValue = "5", required = true)
          @RequestParam(value = "size", defaultValue = "5")
          int size) {
    return ResponseEntity.ok(videoService.getAllVideoForAdmin(PageRequest.of(page - 1, size)));
  }

  @GetDetailVideoForAdminSwagger
  @GetMapping(value = "/videos/{videoId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole(ROLE_ADMIN)")
  public ResponseEntity<GetVideoDetailForAdminDto> getVideoById(
      @ApiParam(name = "영상 ID", required = true) @PathVariable Long videoId) {
    return ResponseEntity.ok(videoService.getVideoByIdForAdmin(videoId));
  }

  @DeleteVideoForAdminSwagger
  @DeleteMapping(value = "/videos/{videoId}")
  @PreAuthorize("hasRole(ROLE_ADMIN)")
  public ResponseEntity<GetAdminDeleteVideoStateDto> deleteVideoById(
      @ApiParam(name = "영상 ID", required = true) @PathVariable Long videoId) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(videoService.deleteVideoByIdForAdmin(videoId));
  }
}
