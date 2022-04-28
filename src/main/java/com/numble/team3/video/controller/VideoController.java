package com.numble.team3.video.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.resolver.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VideoController {

  @PostMapping("/video")
  public ResponseEntity createVideo(@LoginUser UserInfo userInfo) {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
