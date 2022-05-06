package com.numble.team3.admin.controller;

import com.numble.team3.account.application.AccountService;
import com.numble.team3.admin.annotation.DeleteTokenSwagger;
import com.numble.team3.admin.annotation.GetAccountSwagger;
import com.numble.team3.admin.annotation.GetAccountsSwagger;
import com.numble.team3.admin.annotation.WithdrawalSwagger;
import com.numble.team3.sign.application.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
@Api(tags = {"회원 관리"})
public class AdminAccountController {

  private final AccountService accountService;
  private final SignService signService;

  @GetAccountsSwagger
  @GetMapping(value = "/accounts/all", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity getAccounts(
    @ApiParam(value = "페이지", required = true) @RequestParam("page") int page,
    @ApiParam(value = "페이지 크기", required = true) @RequestParam("size") int size) {
    return ResponseEntity.ok(accountService.getAccounts(PageRequest.of(page - 1, size)));
  }

  @GetAccountSwagger
  @GetMapping(value = "/accounts/{id}", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity getAccount(@PathVariable Long id) {
    return ResponseEntity.ok(accountService.getAccountByAdmin(id));
  }

  @WithdrawalSwagger
  @DeleteMapping(value = "/accounts/withdrawal/{id}", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity withdrawalAccount(@PathVariable Long id) {
    accountService.withdrawalAccount(id);
    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteTokenSwagger
  @DeleteMapping(value = "/accounts/access-token/{id}", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity deleteAccessToken(@PathVariable Long id) {
    signService.deleteToken(id);
    return new ResponseEntity(HttpStatus.OK);
  }

}