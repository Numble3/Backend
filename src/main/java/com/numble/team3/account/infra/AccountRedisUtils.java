package com.numble.team3.account.infra;

import com.numble.team3.account.domain.AccountUtils;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRedisUtils implements AccountUtils {

  private final AccountRedisHelper accountRedisHelper;

  @Override
  public List<Long> getAllLastLoginAccountId() {
    return accountRedisHelper.getAllLastLoginKey();
  }

  @Override
  public String getAccountLastLoginTime(Long accountId) {
    return accountRedisHelper.getLastLogin(accountId);
  }

  @Override
  public Optional<String> optionalGetAccountLastLoginTime(Long accountId) {
    return Optional.ofNullable(accountRedisHelper.getLastLogin(accountId));
  }

  @Override
  public void deleteAllLastLoginTime(Long accountId) {
    accountRedisHelper.deleteLastLogin(accountId);
  }
}
