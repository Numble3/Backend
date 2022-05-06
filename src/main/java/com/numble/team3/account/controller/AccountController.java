package com.numble.team3.account.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.application.AccountService;
import com.numble.team3.account.application.request.UpdateMyAccountDto;
import com.numble.team3.account.resolver.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

  private final AccountService accountService;

  @GetMapping(value = "/accounts", produces = "application/json")
  public ResponseEntity getMyAccount(@LoginUser UserInfo userInfo) {
    return ResponseEntity.ok(accountService.getMyAccount(userInfo));
  }

  @PostMapping(value = "/accounts/update", produces = "application/json")
  public ResponseEntity updateMyAccount(
    @LoginUser UserInfo userInfo,
    @RequestBody UpdateMyAccountDto dto) {
    accountService.updateMyAccount(userInfo, dto);
    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping(value = "/accounts/withdrawal", produces = "application/json")
  public ResponseEntity withdrawalAccount(@LoginUser UserInfo userInfo) {
    accountService.withdrawalAccount(userInfo.getAccountId());
    return new ResponseEntity(HttpStatus.OK);
  }
}
