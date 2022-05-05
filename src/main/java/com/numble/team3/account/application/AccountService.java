package com.numble.team3.account.application;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.domain.AccountUtils;
import com.numble.team3.account.infra.AccountRedisHelper;
import com.numble.team3.account.infra.JpaAccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

  private final JpaAccountRepository accountRepository;
  private final AccountUtils accountUtils;

  public void changeAccountLastLoginByScheduler() {
    List<Account> accounts = accountRepository.findAll();

    List<Long> accountIds = accountUtils.getAllLastLoginAccountId();

    accounts
      .stream().filter(account -> accountIds.contains(account.getId()))
      .forEach(account -> {
        String lastLoginTime =
          accountUtils.getAccountLastLoginTime(account.getId());
        if (!(account.getLastLogin() != null && account.getLastLogin().equals(lastLoginTime))) {
          account.changeLastLogin();
        }});

    accountIds.stream().forEach(accountId -> accountUtils.deleteAllLastLoginTime(accountId));
  }
}
