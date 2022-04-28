package com.numble.team3.video.application.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.video.application.VideoService;
import com.numble.team3.video.application.request.CreateVideoDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VideoController {

  @PostMapping("/video")
  public ResponseEntity createVideo(@LoginUser UserInfo userInfo) {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
