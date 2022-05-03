package com.numble.team3.account.scheduler;

import com.numble.team3.account.application.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountLastLoginScheduler {

  private final AccountService accountService;

  @Scheduled(cron = "0 0 0 * * *")
  protected void changeAccountLastLogin() {
    accountService.changeAccountLastLoginByScheduler();
  }
}
