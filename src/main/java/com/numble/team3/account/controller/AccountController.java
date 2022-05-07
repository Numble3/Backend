package com.numble.team3.account.controller;

import com.numble.team3.account.annotation.GetMyAccountSwagger;
import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.annotation.UpdateMyAccountSwagger;
import com.numble.team3.account.annotation.WithdrawalAccountSwagger;
import com.numble.team3.account.application.AccountService;
import com.numble.team3.account.application.request.UpdateMyAccountDto;
import com.numble.team3.account.resolver.UserInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"회원 정보 조회, 회원 정보 수정, 회원 탈퇴"})
public class AccountController {

  private final AccountService accountService;

  @GetMyAccountSwagger
  @GetMapping(value = "/accounts", produces = "application/json")
  public ResponseEntity getMyAccount(@ApiIgnore @LoginUser UserInfo userInfo) {
    return ResponseEntity.ok(accountService.getMyAccount(userInfo));
  }

  @UpdateMyAccountSwagger
  @PostMapping(value = "/accounts/update", produces = "application/json")
  public ResponseEntity updateMyAccount(
    @ApiIgnore @LoginUser UserInfo userInfo,
    @RequestBody UpdateMyAccountDto dto) {
    accountService.updateMyAccount(userInfo, dto);
    return new ResponseEntity(HttpStatus.OK);
  }

  @WithdrawalAccountSwagger
  @DeleteMapping(value = "/accounts/withdrawal", produces = "application/json")
  public ResponseEntity withdrawalAccount(@ApiIgnore @LoginUser UserInfo userInfo) {
    accountService.withdrawalAccount(userInfo.getAccountId());
    return new ResponseEntity(HttpStatus.OK);
  }
}
