package com.numble.team3.factory.entity;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.domain.RoleType;
import java.lang.reflect.Constructor;

public class AccountEntityFactory {

  public static Account createAccount(Long id, String email, String password, String nickname, RoleType roleType) throws Exception {
    Constructor<Account> accountConstructor = Account.class.getDeclaredConstructor();
    accountConstructor.setAccessible(true);

    Account account = accountConstructor.newInstance();
    setField(account, "id", id);
    setField(account, "email", email);
    setField(account, "password", password);
    setField(account, "nickname", nickname);
    setField(account, "roleType", roleType);

    return account;
  }

  public static Account createDeletedAccount(Long id, String email, String password, String nickname, RoleType roleType) throws Exception {
    Account account = AccountEntityFactory.createAccount(id, email, password, nickname, roleType);
    setField(account, "deleted", true);

    return account;
  }
}
