package com.numble.team3.account.controller;

import com.numble.team3.account.annotation.GetMyAccountDetailSwagger;
import com.numble.team3.account.annotation.GetMyAccountSwagger;
import com.numble.team3.account.annotation.GetMyVideosSwagger;
import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.annotation.UpdateMyAccountSwagger;
import com.numble.team3.account.annotation.WithdrawalAccountSwagger;
import com.numble.team3.account.application.AccountService;
import com.numble.team3.account.application.request.UpdateMyAccountDto;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.video.domain.enums.VideoSortCondition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"회원 정보 조회, 회원 정보 상세 조회, 업로드 영상 조회, 회원 정보 수정, 회원 탈퇴"})
public class AccountController {

  private final AccountService accountService;

  @GetMyAccountSwagger
  @GetMapping(value = "/accounts", produces = "application/json")
  public ResponseEntity getMyAccount(@ApiIgnore @LoginUser UserInfo userInfo) {
    return ResponseEntity.ok(accountService.getAccountByUserInfo(userInfo));
  }

  @GetMyAccountDetailSwagger
  @GetMapping(value = "/accounts/detail", produces = "application/json")
  public ResponseEntity getMyAccountDetail(@ApiIgnore @LoginUser UserInfo userInfo) {
    return ResponseEntity.ok(accountService.getDetailAccountByUserInfo(userInfo));
  }

  @UpdateMyAccountSwagger
  @PostMapping(value = "/accounts/update", produces = "application/json")
  public ResponseEntity updateMyAccount(
    @ApiIgnore @LoginUser UserInfo userInfo,
    @RequestBody UpdateMyAccountDto dto) {
    accountService.updateAccount(userInfo, dto);
    return new ResponseEntity(HttpStatus.OK);
  }

  @WithdrawalAccountSwagger
  @DeleteMapping(value = "/accounts/withdrawal", produces = "application/json")
  public ResponseEntity withdrawalAccount(@ApiIgnore @LoginUser UserInfo userInfo) {
    accountService.withdrawalAccount(userInfo.getAccountId());
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMyVideosSwagger
  @GetMapping(value = "/accounts/videos", produces = "application/json")
  public ResponseEntity getMyVideos(
    @ApiIgnore @LoginUser UserInfo userInfo,
    @ApiParam(value = "video id, 2페이지 이후 조회를 위한 값, null이거나 0일 경우 1페이지 조회", required = false)
    @RequestParam(required = false, name = "id") Long videoId,
    @ApiParam(value = "비디오 정렬 기준, 기본값 최신순(latest, popularity)", required = false)
    @RequestParam(required = false, defaultValue = "latest") String sort) {

    return new ResponseEntity(
      accountService.getMyVideosDto(VideoSortCondition.from(sort), userInfo, videoId), HttpStatus.OK);
  }
}
