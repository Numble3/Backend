package com.numble.team3.factory;

import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.resolver.UserInfo;
import java.util.List;

public class UserInfoFactory {

  public static UserInfo createUserInfo(Long accountId, RoleType roleType) {
    return new UserInfo(accountId, List.of(roleType.toString()));
  }
}
