package com.numble.team3.admin.controller;

import com.numble.team3.admin.application.response.GetVideoDetailForAdminDto;
import com.numble.team3.admin.application.response.GetVideoListForAdminDto;
import com.numble.team3.video.application.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminVideoController {
  private final VideoService videoService;

  @GetMapping(value = "/videos", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole(ROLE_ADMIN)")
  public ResponseEntity<GetVideoListForAdminDto> getAllVideo(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "5") int size) {
    return ResponseEntity.ok(videoService.getAllVideoForAdmin(PageRequest.of(page - 1, size)));
  }

  @GetMapping(value = "/videos/{videoId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole(ROLE_ADMIN)")
  public ResponseEntity<GetVideoDetailForAdminDto> getVideoById(@PathVariable Long videoId) {
    return ResponseEntity.ok(videoService.getVideoByIdForAdmin(videoId));
  }

  @DeleteMapping(value = "/videos/{videoId}")
  @PreAuthorize("hasRole(ROLE_ADMIN)")
  public ResponseEntity deleteVideoById(@PathVariable Long videoId) {
    videoService.deleteVideoByIdForAdmin(videoId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
