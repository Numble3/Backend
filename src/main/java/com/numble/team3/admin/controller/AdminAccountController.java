package com.numble.team3.admin.controller;

import com.numble.team3.account.application.AccountService;
import com.numble.team3.sign.application.SignService;
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
public class AdminAccountController {

  private final AccountService accountService;
  private final SignService signService;

  @GetMapping(value = "/accounts/all", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity getAccounts(
    @RequestParam("page") int page,
    @RequestParam("size") int size) {
    return ResponseEntity.ok(accountService.getAccounts(PageRequest.of(page - 1, size)));
  }

  @GetMapping(value = "/accounts/{id}", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity getAccount(@PathVariable Long id) {
    return ResponseEntity.ok(accountService.getAccountByAdmin(id));
  }

  @DeleteMapping(value = "/accounts/withdrawal/{id}", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity withdrawalAccount(@PathVariable Long id) {
    accountService.withdrawalAccount(id);
    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping(value = "/accounts/access-token/{id}", produces = "application/json")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity deleteAccessToken(@PathVariable Long id) {
    signService.deleteToken(id);
    return new ResponseEntity(HttpStatus.OK);
  }

}