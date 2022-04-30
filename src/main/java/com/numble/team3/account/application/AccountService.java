package com.numble.team3.account.application;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.infra.AccountRedisUtils;
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
  private final AccountRedisUtils accountRedisUtils;

  public void changeAccountLastLoginByScheduler() {
    List<Account> accounts = accountRepository.findAll();

    List<Long> accountIds = accountRedisUtils.getAllLastLoginKey();

    accounts
      .stream().filter(account -> accountIds.contains(account.getId()))
      .forEach(account -> {
        String lastLoginTime =
          accountRedisUtils.getLastLogin(account.getId()).orElseThrow(RuntimeException::new);
        if (!(account.getLastLogin() != null && account.getLastLogin().equals(lastLoginTime))) {
          account.changeLastLogin(lastLoginTime);
        }});

    accountIds.stream().forEach(accountId -> accountRedisUtils.deleteLastLogin(accountId));
  }
}
