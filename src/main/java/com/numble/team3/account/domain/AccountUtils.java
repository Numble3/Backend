package com.numble.team3.account.domain;

import java.util.List;
import java.util.Optional;

public interface AccountUtils {

  List<Long> getAllLastLoginAccountId();

  String getAccountLastLoginTime(Long accountId);

  Optional<String> optionalGetAccountLastLoginTime(Long accountId);

  void deleteAllLastLoginTime(Long accountId);

}
